/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

import org.tablevert.core.BuilderFailedException;

/**
 * Query for retrieving data from a database.
 */
public class DatabaseQuery {

    public static class Builder {
        private String name;
        private String databaseName;
        private String queryStatement;

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder accessingDatabase(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        public Builder withStatement(String queryStatement) {
            this.queryStatement = queryStatement;
            return this;
        }

        public DatabaseQuery build() throws BuilderFailedException {
            validate();
            DatabaseQuery query = new DatabaseQuery();
            query.name = this.name;
            query.databaseName = this.databaseName;
            query.queryStatement = this.queryStatement;
            return query;
        }

        private void validate() throws BuilderFailedException {
            String errors = "";
            if (name == null || name.isEmpty()) {
                errors += "- query name not specified";
            }
            if (databaseName == null || databaseName.isEmpty()) {
                errors += "- database name not specified";
            }
            if (queryStatement == null || queryStatement.isEmpty()) {
                errors += "- query statement not specified";
            } else if (!"select".equals(queryStatement.substring(0, 6).toLowerCase())) {
                errors += "- query statement [" + queryStatement + "] does not start with 'select' (case-insensitive)";
            }
            if (!errors.isEmpty()) {
                throw new BuilderFailedException("Builder validation failed with errors: " + errors);
            }
        }
    }

    private String name;
    private String databaseName;
    private String queryStatement;

    private DatabaseQuery() {
    }

    /**
     * @return the name of the query
     */
    public String getName() {
        return name;
    }

    /**
     * @return the name of the database to access
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * @return the query statement
     */
    public String getQueryStatement() {
        return queryStatement;
    }

    public DatabaseQuery clone() {
        try {
            return new Builder()
                    .withName(this.name)
                    .accessingDatabase(this.databaseName)
                    .withStatement(this.queryStatement)
                    .build();
        } catch (BuilderFailedException e) {
            throw new IllegalStateException("Builder should never fail in clone()");
        }
    }

}
