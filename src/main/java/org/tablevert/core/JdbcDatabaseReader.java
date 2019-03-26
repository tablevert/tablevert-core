/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.tablevert.core.config.Database;
import org.tablevert.core.config.DatabaseQuery;
import org.tablevert.core.config.DatabaseType;

import java.sql.*;

/**
 * {@link DatabaseReader} implementation for JDBC database access.
 */
class JdbcDatabaseReader implements DatabaseReader {

    static class Builder implements DatabaseReader.Builder {
        private static final int DEFAULT_PORT_POSTGRESQL = 5432;

        private JdbcDatabaseReader dbReader;
        private Database database;
        private String userName;
        private Integer port;

        @Override
        public Builder forDatabase(Database database) {
            this.database = database;
            return this;
        }

        @Override
        public Builder withUser(String userName) {
            this.userName = userName;
            return this;
        }

        @Override
        public JdbcDatabaseReader build() throws BuilderFailedException {
            if (port == null) {
                port = selectDefaultPort();
            }
            validate();
            dbReader = new JdbcDatabaseReader();
            dbReader.setConnectionString(assembleConnectionString());
            dbReader.setCredentials(userName, database.getUserSecret(userName));
            return dbReader;
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
            if (database == null) {
                errors += "- database not specified";
            } else {
                try {
                    Class.forName(selectJdbcDriverClassName());
                } catch (ClassNotFoundException e) {
                    errors += "- driver class is missing for database type [" + database.getDbType().getName() + "]";
                }
            }
            if (userName == null || userName.isEmpty()) {
                errors += "- user not specified";
            }
            if (!errors.isEmpty()) {
                throw new BuilderFailedException("Builder validation failed with errors: " + errors);
            }
        }

        private Integer selectDefaultPort() throws BuilderFailedException {
            if (database == null) {
                return null;
            }
            switch (database.getDbType()) {
                case POSTGRESQL:
                    return DEFAULT_PORT_POSTGRESQL;
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

    private String connectionString;
    private String userName;
    private String userSecret;

    private JdbcDatabaseReader() {
    }

    // TODO: Switch to applied query
    public DataGrid fetchData(String queryStatement) throws Exception {
        try (Connection connection = openConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryStatement);

            ResultSetMetaData metaData = resultSet.getMetaData();

            DataGrid dataGrid = new DataGrid();
            int columnCount = extractColumns(dataGrid, metaData);
            extractRows(dataGrid, resultSet, columnCount);

            return dataGrid;
        }
    }

    private int extractColumns(DataGrid dataGrid, ResultSetMetaData metaData) throws SQLException {
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            DataGridColumn column = new DataGridColumn(i - 1, metaData.getColumnName(i), metaData.getColumnClassName(i));
            dataGrid.addColumn(column);
        }

        return metaData.getColumnCount();
    }

    private long extractRows(DataGrid dataGrid, ResultSet resultSet, int columnCount) throws SQLException {
        int rowCount = 0;
        while (resultSet.next()) {
            dataGrid.addRow(extractRow(rowCount, resultSet, columnCount));
            rowCount++;
        }
        return rowCount;
    }

    private DataGridRow extractRow(int rowIndex, ResultSet resultSet, int columnCount) throws SQLException {
        DataGridRow row = new DataGridRow(rowIndex);
        for (int i = 1; i <= columnCount; i++) {
            Object value = resultSet.getObject(i);
            if (value != null) {
                row.addReplaceValue(i - 1, value);
            }
        }
        return row;
    }

    private void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    private void setCredentials(String userName, String userSecret) {
        this.userName = userName;
        this.userSecret = userSecret;
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
