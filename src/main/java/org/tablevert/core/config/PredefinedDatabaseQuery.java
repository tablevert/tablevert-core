/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

import org.tablevert.core.BuilderFailedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Pre-configured query for retrieving data from a database.
 */
public class PredefinedDatabaseQuery implements PredefinedQuery, Cloneable {

    public static class Builder {
        private String name;
        private String databaseName;
        private List<DatabaseQueryColumn> columnsToSelect;
        private String fromClause;
        private String whereClause;
        private List<String> sorting;

        /**
         * Assigns the name by which the query is referenced.
         * @param name the query name
         * @return the builder
         */
        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Assigns the name of the database the query accesses.
         * @param databaseName the database name
         * @return the builder
         */
        public Builder accessingDatabase(String databaseName) {
            this.databaseName = databaseName;
            return this;
        }

        /**
         * Defines the {@code SELECT} part of the query.
         * @param columnsToSelect the columns to select
         * @return the builder
         */
        public Builder selectingColumns(List<DatabaseQueryColumn> columnsToSelect) {
            this.columnsToSelect = new ArrayList<>();
            columnsToSelect.forEach(column -> this.columnsToSelect.add(column.clone()));
            return this;
        }

        /**
         * Defines the table (join) from which to select the data.
         * @param fromClause the table or table join
         * @return the builder
         */
        public Builder selectingFrom(String fromClause) {
            this.fromClause = fromClause;
            return this;
        }

        /**
         * Defines the {@code WHERE} clause of the query.
         * @param whereClause the {@code WHERE} clause
         * @return the builder
         */
        public Builder applyingFilter(String whereClause) {
            this.whereClause = whereClause;
            return this;
        }

        /**
         * Defines the {@code ORDER BY} clause of the query.
         * @param sorting the {@code ORDER BY} clause
         * @return the builder
         */
        public Builder withSorting(List<String> sorting) {
            this.sorting = sorting == null ? null : new ArrayList<>(sorting);
            return this;
        }

        /**
         * Builds a {@link PredefinedDatabaseQuery} object from the parameters passed to the builder.
         * @return the query object
         * @throws BuilderFailedException a builder-related exception
         */
        public PredefinedDatabaseQuery build() throws BuilderFailedException {
            validate();
            PredefinedDatabaseQuery query = new PredefinedDatabaseQuery();
            query.name = this.name;
            query.databaseName = this.databaseName;
            query.columnsToSelect = this.columnsToSelect;
            query.fromClause = this.fromClause;
            query.whereClause = this.whereClause;
            query.sorting = this.sorting;
            return query;
        }

        private void validate() throws BuilderFailedException {
            String errors = "";
            if (name == null || name.isEmpty()) {
                errors += " - query name not specified";
            }
            if (databaseName == null || databaseName.isEmpty()) {
                errors += " - database name not specified";
            }
            errors += detectColumnErrors();
            if (fromClause == null || fromClause.isEmpty()) {
                errors += " - from clause not specified";
            }
            if (!errors.isEmpty()) {
                throw new BuilderFailedException("Builder validation failed with errors: " + errors);
            }
        }

        private String detectColumnErrors() {
            if (columnsToSelect == null || columnsToSelect.isEmpty()) {
                return "- select columns not specified";
            }
            String errors = "";
            for (int i = 0; i < columnsToSelect.size(); i++) {
                String singleErrors = detectSingleColumnErrors(columnsToSelect.get(i));
                if (!singleErrors.isEmpty()) {
                    errors += " - column at " + String.valueOf(i) + ": " + singleErrors;
                }
            }
            return errors;
        }

        private String detectSingleColumnErrors(DatabaseQueryColumn queryColumn) {
            if (queryColumn == null) {
                return "column is null";
            }
            String errors = "";
            String colName = queryColumn.getName();
            if (colName == null || colName.isEmpty()) {
                errors += "name is not specified";
            } else if (colName.contains(" ")) {
                errors += "name contains whitespace(s)";
            }
            return errors;
        }

    }

    private String name;
    private String databaseName;
    private List<DatabaseQueryColumn> columnsToSelect;
    private String fromClause;
    private String whereClause;
    private List<String> sorting;

    private PredefinedDatabaseQuery() {
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
     * @return the name of the database to access
     */
    @Override
    public String getDataSourceName() {
        return databaseName;
    }

    /**
     * Gets a deep clone of the query's select columns.
     * @return the select columnns
     */
    public List<DatabaseQueryColumn> getColumnsToSelect() {
        List<DatabaseQueryColumn> columns = new ArrayList<>();
        columnsToSelect.forEach(column -> columns.add(column.clone()));
        return columns;
    }

    /**
     * Gets the {@code FROM} clause of the query statement.
     * @return the {@code FROM} clause
     */
    public String getFromClause() {
        return this.fromClause;
    }

    /**
     * Gets the {@code WHERE} clause of the query statement.
     * @return the {@code WHERE} clause
     */
    public String getWhereClause() {
        return this.whereClause;
    }

    /**
     * Gets a clone of the sort columns.
     * If a column name is prefixed with a minus character ('-'), it will be ordered descendingly.
     * @return the sort columns
     */
    public List<String> getSorting() {
        if (sorting == null) {
            return null;
        }
        return new ArrayList<>(sorting);
    }

    /**
     * Creates a deep clone of the object.
     * @return the clone
     */
    @Override
    public PredefinedDatabaseQuery clone() {
        try {
            return new Builder()
                    .withName(this.name)
                    .accessingDatabase(this.databaseName)
                    .selectingColumns(this.columnsToSelect)
                    .selectingFrom(this.fromClause)
                    .applyingFilter(this.whereClause)
                    .withSorting(this.sorting)
                    .build();
        } catch (BuilderFailedException e) {
            throw new IllegalStateException("Builder should never fail in clone()");
        }
    }

}
