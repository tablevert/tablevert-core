/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.tablevert.core.config.*;

import java.sql.*;
import java.util.*;

/**
 * {@link DatabaseReader} implementation for JDBC database access.
 */
class JdbcDatabaseReader implements DatabaseReader {

    static class Builder implements DatabaseReader.Builder {
        private static final int DEFAULT_PORT_POSTGRESQL = 5432;

        private JdbcDatabaseReader dbReader;
        private Database database;
        private DatabaseQuery databaseQuery;
        private AppliedQuery appliedQuery;
        private TablevertConfig tablevertConfig;
        private Integer port;

        @Override
        public Builder usingConfig(TablevertConfig tablevertConfig) {
            this.tablevertConfig = tablevertConfig;
            return this;
        }

        @Override
        public Builder forAppliedQuery(AppliedQuery appliedQuery) {
            this.appliedQuery = appliedQuery;
            return this;
        }

        @Override
        public JdbcDatabaseReader build() throws BuilderFailedException {
            initDatabaseAndQuery();
            checkInitPort();
            validate();
            dbReader = new JdbcDatabaseReader();
            dbReader.databaseType = database.getDbType();
            dbReader.connectionString = assembleConnectionString();
            dbReader.databaseQuery = databaseQuery;
            dbReader.appliedQuery = appliedQuery;
            dbReader.userName = database.getDefaultUserName();
            dbReader.userSecret = database.getUserSecret(dbReader.userName);
            return dbReader;
        }

        private void initDatabaseAndQuery() {
            if (tablevertConfig != null && appliedQuery != null) {
                this.database = tablevertConfig.getDatabaseForQuery(appliedQuery.getBaseQueryName());
                this.databaseQuery = tablevertConfig.getDatabaseQuery(appliedQuery.getBaseQueryName());
            }
        }

        private String assembleConnectionString() {
            return "jdbc:"
                    + database.getDbType().getName() + "://"
                    + database.getHost() + ":"
                    + port + "/"
                    + database.getName();
        }

        private void validate() throws BuilderFailedException {
            String errors = "";
            if (tablevertConfig == null) {
                errors += " - Tablevert configuration not specified;";
            }
            if (appliedQuery == null) {
                errors += " - applied query not specified;";
            }
            if (database == null) {
                errors += " - database not configured or not specified;";
            } else {
                try {
                    Class.forName(selectJdbcDriverClassName());
                } catch (ClassNotFoundException e) {
                    errors += " - driver class is missing for database type [" + database.getDbType().getName() + "];";
                }
            }
            if (databaseQuery == null) {
                errors += " - base query for applied query not configured;";
            }
            String userName = database == null ? null : database.getDefaultUserName();
            if (userName == null || userName.isEmpty()) {
                errors += " - user not specified;";
            }
            else if (database != null && database.getUserSecret(userName) == null) {
                errors += " - user [" + userName + "] not configured for database ["
                        + (database.getName() == null ? "??" : database.getName()) + "];";
            }
            if (!errors.isEmpty()) {
                throw new BuilderFailedException("Builder validation failed with errors: " + errors);
            }
        }

        private void checkInitPort() throws BuilderFailedException {
            if (port != null || database == null) {
                return;
            }
            switch (database.getDbType()) {
                case POSTGRESQL:
                    port = DEFAULT_PORT_POSTGRESQL;
                    return;
                default:
                    throw new BuilderFailedException("No default port available for database type ["
                            + database.getDbType().name() + "]");
            }
        }

        private String selectJdbcDriverClassName() throws BuilderFailedException {
            switch (database.getDbType()) {
                case POSTGRESQL:
                    return "org.postgresql.Driver";
                default:
                    throw new BuilderFailedException("No JDBC driver class name available for database type ["
                            + database.getDbType().name() + "]");
            }
        }
    }

    private DatabaseType databaseType;
    private String connectionString;
    private String userName;
    private String userSecret;
    private DatabaseQuery databaseQuery;
    private AppliedQuery appliedQuery;

    private JdbcDatabaseReader() {
    }

    @Override
    public DataGrid read() throws TablevertCoreException {
        try (Connection connection = openConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(composeQueryStatement());

            ResultSetMetaData metaData = resultSet.getMetaData();

            DataGrid dataGrid = new DataGrid();
            int columnCount = extractColumns(dataGrid, metaData);
            extractRows(dataGrid, resultSet, columnCount);

            return dataGrid;
        } catch (SQLException e) {
            throw new DatabaseReaderException(e);
        }
    }

    private String composeQueryStatement() {
        // TODO: Integrate applied filtering and sorting
        List<DatabaseQueryColumn> columnsToSelect = databaseQuery.getColumnsToSelect();

        String queryStatement = String.format(databaseType.getSelectStatementTemplate(),
                prepareColumns(columnsToSelect),
                prepareSource(databaseQuery.getFromClause()),
                prepareFilter(databaseQuery.getWhereClause()),
                "");

        return queryStatement;
    }

    private String prepareColumns(List<DatabaseQueryColumn> columnsToSelect) {
        List<String> columns = new ArrayList<>();
        columnsToSelect.forEach(column ->
                columns.add((column.getFormula() == null ? "" : (column.getFormula() + " AS ")) + column.getName()));
        return String.join(",", columns);
    }

    private String prepareSource(String from) {
        return from;
    }

    private String prepareFilter(String baseClause) {
        if (baseClause == null || baseClause.trim().isEmpty()) {
            return "";
        }
        return " WHERE " + baseClause;
    }


    private int extractColumns(DataGrid dataGrid, ResultSetMetaData metaData) throws SQLException {
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            DataGridColumn column = new DataGridColumn(i - 1, metaData.getColumnName(i), metaData.getColumnClassName(i));
            dataGrid.addColumn(column);
        }

        return metaData.getColumnCount();
    }

    private long extractRows(DataGrid dataGrid, ResultSet resultSet, int columnCount) throws SQLException {
        List<JdbcValueConversionType> conversionTypes = getConversionTypes(dataGrid.cloneColumns());
        int rowCount = 0;
        while (resultSet.next()) {
            dataGrid.addRow(extractSingleRow(rowCount, resultSet, columnCount, conversionTypes));
            rowCount++;
        }
        return rowCount;
    }

    private DataGridRow extractSingleRow(int rowIndex, ResultSet resultSet, int columnCount,
                                         List<JdbcValueConversionType> conversionTypes) throws SQLException {
        DataGridRow row = new DataGridRow(rowIndex);
        for (int i = 0; i < columnCount; i++) {
            Object value = getSingleValue(resultSet, i + 1, conversionTypes.get(i));
            if (value != null) {
                row.addReplaceValue(i, value);
            }
        }
        return row;
    }

    private Object getSingleValue(ResultSet resultSet, int index, JdbcValueConversionType conversionType) throws SQLException {
        if (conversionType == null || JdbcValueConversionType.STRING.equals(conversionType)) {
            return resultSet.getString(index);
        }
        return resultSet.getObject(index);
    }

    private List<JdbcValueConversionType> getConversionTypes(List<DataGridColumn> columns) {
        List<JdbcValueConversionType> conversionTypes = new ArrayList<>();
        columns.stream()
                .sorted(Comparator.comparingInt(col -> col.getIndex()))
                .forEach(col -> conversionTypes.add(col.getJavaClassName().startsWith("java")
                        ? JdbcValueConversionType.OBJECT : JdbcValueConversionType.STRING));
        return conversionTypes;
    }

    private Connection openConnection() throws DatabaseReaderException {
        try {
            return DriverManager.getConnection(connectionString, userName, userSecret);
        } catch (SQLException e) {
            throw new DatabaseReaderException("Failed to open connection for connection string: "
                    + connectionString);
        }
    }

}
