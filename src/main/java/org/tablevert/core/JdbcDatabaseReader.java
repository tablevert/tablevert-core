/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tablevert.core.config.*;

import java.sql.*;
import java.util.*;

/**
 * {@link DatabaseReader} implementation for JDBC database access.
 */
class JdbcDatabaseReader implements DatabaseReader {

    static class Builder implements DatabaseReader.Builder {
        private JdbcDatabaseReader dbReader;
        private Database database;
        private PredefinedDatabaseQuery predefinedDatabaseQuery;
        private AppliedQuery appliedQuery;
        private TablevertConfig tablevertConfig;

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
            validate();
            dbReader = new JdbcDatabaseReader();
            dbReader.databaseType = database.getDbType();
            dbReader.connectionString = assembleConnectionString();
            dbReader.predefinedDatabaseQuery = predefinedDatabaseQuery;
            dbReader.appliedQuery = appliedQuery;
            dbReader.userName = database.getDefaultUserName();
            dbReader.userSecret = database.getUserSecret(dbReader.userName);
            return dbReader;
        }

        private void initDatabaseAndQuery() throws BuilderFailedException {
            if (tablevertConfig != null && appliedQuery != null) {
                DataSource dataSource = tablevertConfig.getDataSourceForQuery(appliedQuery.getBaseQueryName());
                if (dataSource != null && !Database.class.equals(dataSource.getClass())) {
                    throw new BuilderFailedException(
                            "Data source is not a database; actual class: [" + dataSource.getClass().getSimpleName() + "]");
                }
                this.database = (Database) dataSource;
                PredefinedQuery predefinedQuery = tablevertConfig.getPredefinedQuery(appliedQuery.getBaseQueryName());
                if (predefinedQuery != null && !PredefinedDatabaseQuery.class.equals(predefinedQuery.getClass())) {
                    throw new BuilderFailedException(
                            "Predefined query does not a database query; actual class: [" + predefinedQuery.getClass().getSimpleName() + "]");
                }
                this.predefinedDatabaseQuery = (PredefinedDatabaseQuery) predefinedQuery;
            }
        }

        private String assembleConnectionString() {
            return "jdbc:"
                    + database.getDbType().getName() + "://"
                    + database.getHost() + ":"
                    + database.getPort() + "/"
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
            if (predefinedDatabaseQuery == null) {
                errors += " - base query for applied query not configured;";
            }
            String userName = database == null ? null : database.getDefaultUserName();
            if (userName == null || userName.isEmpty()) {
                errors += " - user not specified;";
            } else if (database != null && database.getUserSecret(userName) == null) {
                errors += " - user [" + userName + "] not configured for database ["
                        + (database.getName() == null ? "??" : database.getName()) + "];";
            }
            if (!errors.isEmpty()) {
                throw new BuilderFailedException("Builder validation failed with errors: " + errors);
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

    private final Logger logger = LoggerFactory.getLogger(JdbcDatabaseReader.class);

    private DatabaseType databaseType;
    private String connectionString;
    private String userName;
    private String userSecret;
    private PredefinedDatabaseQuery predefinedDatabaseQuery;
    private AppliedQuery appliedQuery;

    private JdbcDatabaseReader() {
    }

    @Override
    public DataGrid read() throws TablevertCoreException {
        try (Connection connection = openConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(composeQueryStatement());

            ResultSetMetaData metaData = resultSet.getMetaData();

            DataGrid.Builder dataGridBuilder = new DataGrid.Builder();
            int columnCount = extractColumns(dataGridBuilder, metaData);
            dataGridBuilder.andData();
            extractRows(dataGridBuilder, resultSet, columnCount);

            return dataGridBuilder.build();
        } catch (SQLException | DataGridException e) {
            throw new DatabaseReaderException(e);
        }
    }

    private String composeQueryStatement() {
        // TODO: Integrate applied filtering and sorting
        List<DatabaseQueryColumn> columnsToSelect = predefinedDatabaseQuery.getColumnsToSelect();

        String queryStatement = String.format(databaseType.getSelectStatementTemplate(),
                prepareColumns(columnsToSelect),
                prepareSource(predefinedDatabaseQuery.getFromClause()),
                prepareFilter(predefinedDatabaseQuery.getWhereClause()),
                prepareSorting(predefinedDatabaseQuery.getSorting()));

        logger.debug("Prepared query statement: " + queryStatement);
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

    private String prepareSorting(List<String> sortColumns) {
        if (sortColumns == null || sortColumns.isEmpty()) {
            return "";
        }
        StringBuilder sorting = new StringBuilder();
        for (String column : sortColumns) {
            String sortedColumn = checkAddSortDirection(column);
            sorting.append((!sortedColumn.isEmpty() && (sorting.length() > 0)) ? "," : "").append(sortedColumn);
        }
        if (sorting.length() == 0) {
            return "";
        }
        return " ORDER BY " + sorting;
    }

    private String checkAddSortDirection(String column) {
        if (column == null) {
            return "";
        }
        if (column.startsWith("-")) {
            return column.substring(1) + " DESC";
        }
        return column;
    }


    private int extractColumns(DataGrid.Builder dataGridBuilder, ResultSetMetaData metaData) throws SQLException, DataGridException {
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            DataGridColumn column = new DataGridColumn(i - 1, metaData.getColumnName(i), metaData.getColumnClassName(i));
            dataGridBuilder.withColumn(column);
        }

        return metaData.getColumnCount();
    }

    private long extractRows(DataGrid.Builder dataGridBuilder, ResultSet resultSet, int columnCount) throws SQLException, DataGridException {
        List<JdbcValueConversionType> conversionTypes = getConversionTypes(dataGridBuilder.definedColumns());
        long rowCount = 0;
        while (resultSet.next()) {
            dataGridBuilder.withRow(extractSingleRow(rowCount, resultSet, columnCount, conversionTypes));
            rowCount++;
        }
        return rowCount;
    }

    private DataGridRow extractSingleRow(long rowIndex, ResultSet resultSet, int columnCount,
                                         List<JdbcValueConversionType> conversionTypes) throws SQLException {
        DataGridRow row = new DataGridRow(String.valueOf(rowIndex));
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
                .sorted(Comparator.comparingInt(DataGridColumn::getIndex))
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
