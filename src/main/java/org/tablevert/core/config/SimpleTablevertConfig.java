/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

import org.tablevert.core.BuilderFailedException;

import javax.xml.crypto.Data;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Simple implementation of the {@link TablevertConfig} interface.
 */
public class SimpleTablevertConfig implements TablevertConfig, Cloneable {

    /**
     * Builder for SimpleTablevertConfig instances.
     */
    public static class Builder {

        private Map<String, Database> databaseMap;
        private final Map<String, DatabaseQuery> queryMap;
        private Map<String, DatabaseUser> userMap;

        /**
         * Instantiates the builder.
         */
        public Builder() {
            this.queryMap = new Hashtable<>();
            this.databaseMap = new Hashtable<>();
            this.userMap = new Hashtable<>();
        }

        /**
         * Adds a {@link Database} instance to the builder.
         * If a database with the same name already exists, it is replaced.
         * @param database the database to add
         * @return the builder
         */
        public Builder withDatabase(Database database) {
            putOrReplace(databaseMap, database.getName(), database);
            return this;
        }

        /**
         * Adds multiple {@link Database} instances to the builder.
         * Calls {@code withDatabase} for each database.
         * @param databases the databases to add
         * @return the builder
         */
        public Builder withDatabases(List<Database> databases) {
            if (databases != null) {
                for (Database database : databases) {
                    withDatabase(database);
                }
            }
            return this;
        }

        /**
         * Adds a {@link DatabaseQuery} instance to the builder.
         * If a query with the same name already exists, it is replaced.
         * @param query the query to add
         * @return the builder
         */
        public Builder withQuery(DatabaseQuery query) {
            putOrReplace(queryMap, query.getName(), query);
            return this;
        }

        /**
         * Adds multiple {@link DatabaseQuery} instances to the builder.
         * Calls {@code withQuery} for each query.
         * @param queries the queries to add
         * @return the builder
         */
        public Builder withQueries(List<DatabaseQuery> queries) {
            if (queries != null) {
                for (DatabaseQuery query : queries) {
                    withQuery(query);
                }
            }
            return this;
        }

        /**
         * Creates a {@link SimpleTablevertConfig} object from the parameters passed to the builder.
         * @return the built configuration object
         */
        public SimpleTablevertConfig build()throws BuilderFailedException {
            validate();
            SimpleTablevertConfig config = new SimpleTablevertConfig();
            config.queryMap = this.queryMap;
            config.databaseMap = this.databaseMap;
            return config;
        }

        private void validate() throws BuilderFailedException {
            String errors = "";
            if (!queryMap.isEmpty()) {
                errors += detectQueryMapErrors();
            }
            if (!errors.isEmpty()) {
                throw new BuilderFailedException("Builder validation failed with errors: " + errors);
            }
        }

        private String detectQueryMapErrors() {
            String errors = "";
            for (Map.Entry<String, DatabaseQuery> mapEntry : queryMap.entrySet()) {
                errors += detectQueryErrors(mapEntry.getValue());
            }
            return errors;
        }

        private String detectQueryErrors(DatabaseQuery query) {
            if (query == null) {
                return  "Query is not defined; ";
            }
            String errors = "";
            if (databaseMap.get(query.getDatabaseName()) == null) {
                errors += "Database [" + query.getDatabaseName() + "] associated with query ["
                        + query.getName() + "] is not configured; ";
            }
            return errors;
        }

        private void putOrReplace(Map map, String key, Object value) {
            if (map.containsKey(key)) {
                map.replace(key, value);
            } else {
                map.put(key, value);
            }
        }
    }

    private Map<String, Database> databaseMap;
    private Map<String, DatabaseQuery> queryMap;

    private SimpleTablevertConfig() {
    }

    public DatabaseQuery getDatabaseQuery(String queryName) {
        DatabaseQuery databaseQuery = queryMap.containsKey(queryName) ? queryMap.get(queryName) : null;
        return databaseQuery == null ? null : databaseQuery.clone();
    }

    public Database getDatabaseForQuery(String queryName) {
        DatabaseQuery databaseQuery = queryMap.containsKey(queryName) ? queryMap.get(queryName) : null;
        return (databaseQuery == null || !databaseMap.containsKey(databaseQuery.getDatabaseName()))
                ? null : databaseMap.get(databaseQuery.getDatabaseName()).clone();
    }

    /**
     * Creates a deep clone of this object.
     * @return the clone
     */
    @Override
    public SimpleTablevertConfig clone() {
        SimpleTablevertConfig config = new SimpleTablevertConfig();
        config.queryMap = new Hashtable<>();
        this.queryMap.forEach((key, value) -> config.queryMap.put(key, value.clone()));
        config.databaseMap = new Hashtable<>();
        this.databaseMap.forEach((key, value) -> config.databaseMap.put(key, value.clone()));
        return config;
    }

}
