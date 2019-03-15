/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link DatabaseReader} implementation for JDBC database access.
 */
class JdbcDatabaseReader implements DatabaseReader {

    static class Builder {
        private static final int DEFAULT_PORT_POSTGRESQL = 5432;

        private static final Logger logger = LoggerFactory.getLogger(Builder.class);
        private JdbcDatabaseReader dbReader;
        private DatabaseType databaseType;
        private String dbName;
        private String hostName;
        private String userName;
        private String userSecret;
        private int port;

        Builder(DatabaseType databaseType) {
            this.databaseType = databaseType;
            this.port = DEFAULT_PORT_POSTGRESQL;
        }

        Builder forHost(String hostName) {
            this.hostName = hostName;
            return this;
        }

        Builder forDatabase(String dbName) {
            this.dbName = dbName;
            return this;
        }

        Builder withCredentials(String userName, String userSecret) {
            this.userName = userName;
            this.userSecret = userSecret;
            return this;
        }

        JdbcDatabaseReader build() throws BuilderFailedException {
            try {

                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException e) {
                throw new BuilderFailedException("Driver class is missing for database type '" + databaseType.getName() + "'");
            }
            String errors = validate();
            if (errors != null) {
                throw new BuilderFailedException("Builder validation failed with errors: " + errors);
            }
            dbReader = new JdbcDatabaseReader();
            dbReader.setConnectionString(assembleConnectionString());
            dbReader.setCredentials(userName, userSecret);
            return dbReader;
        }

        private String assembleConnectionString() {
            return "jdbc:"
                    + databaseType.getName() + "://"
                    + hostName + ":"
                    + port + "/"
                    + dbName;
        }

        private String validate() {
            String errors = "";
            if (hostName == null || hostName.isEmpty()) {
                errors += "- host name not specified";
            }
            if (dbName == null || dbName.isEmpty()) {
                errors += "- database name not specified";
            }
            if (userName == null || userName.isEmpty() || userSecret == null || userSecret.isEmpty()) {
                errors += "- invalid credentials";
            }
            if (!errors.isEmpty()) {
                logger.error("Could not build JdbcDatabaseReader object: " + errors);
                return errors;
            }
            return null;
        }
    }

    private String connectionString;
    private String userName;
    private String userSecret;

    private JdbcDatabaseReader() {
    }

    public DataGrid fetchData(DatabaseQuery databaseQuery) throws Exception {
        try (Connection connection = openConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(databaseQuery.getQuery());

            ResultSetMetaData metaData = resultSet.getMetaData();

            DataGrid dataGrid = new DataGrid(databaseQuery.getName());
            int columnCount = extractColumns(dataGrid, metaData);
            extractRows(dataGrid, resultSet, columnCount);

            return dataGrid;
        }
    }

    private int extractColumns(DataGrid dataGrid, ResultSetMetaData metaData) throws SQLException {
        List<DataGridColumn> columns = new ArrayList<>();

        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            DataGridColumn column = new DataGridColumn(metaData.getColumnName(i), metaData.getColumnClassName(i));
            dataGrid.addColumn(column);
        }

        return metaData.getColumnCount();
    }

    private long extractRows(DataGrid dataGrid, ResultSet resultSet, int columnCount) throws SQLException {
        long rowCount = 0L;
        while (resultSet.next()) {
            dataGrid.addRow(extractRow(resultSet, columnCount));
            rowCount++;
        }
        return rowCount;
    }

    private DataGridRow extractRow(ResultSet resultSet, int columnCount) throws SQLException {
        DataGridRow row = new DataGridRow();
        for (int i = 1; i <= columnCount; i++) {
            Object value = resultSet.getObject(i);
            if (value != null) {
                row.addReplaceValue(i, value);
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
