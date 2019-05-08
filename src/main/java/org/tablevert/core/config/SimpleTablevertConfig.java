/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

import org.tablevert.core.BuilderFailedException;

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

        private Map<String, DataSource> dataSourceMap;
        private final Map<String, PredefinedQuery> queryMap;

        /**
         * Instantiates the builder.
         */
        public Builder() {
            this.queryMap = new Hashtable<>();
            this.dataSourceMap = new Hashtable<>();
        }

        /**
         * Adds a {@link DataSource} instance to the builder.
         * If a database with the same name already exists, it is replaced.
         * @param dataSource the database to add
         * @return the builder
         */
        public Builder withDataSource(DataSource dataSource) {
            putOrReplace(dataSourceMap, dataSource.getName(), dataSource);
            return this;
        }

        /**
         * Adds multiple {@link DataSource} instances to the builder.
         * Calls {@code withDataSource} for each database.
         * @param dataSources the data sources to add
         * @return the builder
         */
        public Builder withDataSources(List<Database> dataSources) {
            if (dataSources != null) {
                for (Database database : dataSources) {
                    withDataSource(database);
                }
            }
            return this;
        }

        /**
         * Adds a {@link PredefinedQuery} instance to the builder.
         * If a query with the same name already exists, it is replaced.
         * @param query the query to add
         * @return the builder
         */
        public Builder withQuery(PredefinedQuery query) {
            putOrReplace(queryMap, query.getName(), query);
            return this;
        }

        /**
         * Adds multiple {@link PredefinedQuery} instances to the builder.
         * Calls {@code withQuery} for each query.
         * @param queries the queries to add
         * @return the builder
         */
        public Builder withQueries(List<PredefinedQuery> queries) {
            if (queries != null) {
                for (PredefinedQuery query : queries) {
                    withQuery(query);
                }
            }
            return this;
        }

        /**
         * Creates a {@link SimpleTablevertConfig} object from the parameters passed to the builder.
         * @return the built configuration object
         * @throws BuilderFailedException a builder-related exception
         */
        public SimpleTablevertConfig build()throws BuilderFailedException {
            validate();
            SimpleTablevertConfig config = new SimpleTablevertConfig();
            config.queryMap = this.queryMap;
            config.dataSourceMap = this.dataSourceMap;
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
            for (Map.Entry<String, PredefinedQuery> mapEntry : queryMap.entrySet()) {
                errors += detectQueryErrors(mapEntry.getValue());
            }
            return errors;
        }

        private String detectQueryErrors(PredefinedQuery query) {
            if (query == null) {
                return  "Query is not defined; ";
            }
            String errors = "";
            if (dataSourceMap.get(query.getDataSourceName()) == null) {
                errors += "Database [" + query.getDataSourceName() + "] associated with query ["
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

    private Map<String, DataSource> dataSourceMap;
    private Map<String, PredefinedQuery> queryMap;

    private SimpleTablevertConfig() {
    }

    /**
     * Gets the pre-defined query with the provided name
     * @param queryName the name of the query
     * @return the query
     */
    public PredefinedQuery getPredefinedQuery(String queryName) {
        PredefinedQuery predefinedQuery = queryMap.containsKey(queryName) ? queryMap.get(queryName) : null;
        return predefinedQuery == null ? null : predefinedQuery.clone();
    }

    /**
     * Gets the data source referenced by the query having the provided name.
     * @param queryName the name of the query
     * @return the data source
     */
    public DataSource getDataSourceForQuery(String queryName) {
        PredefinedQuery predefinedDatabaseQuery = queryMap.containsKey(queryName) ? queryMap.get(queryName) : null;
        return (predefinedDatabaseQuery == null || !dataSourceMap.containsKey(predefinedDatabaseQuery.getDataSourceName()))
                ? null : dataSourceMap.get(predefinedDatabaseQuery.getDataSourceName()).clone();
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
        config.dataSourceMap = new Hashtable<>();
        this.dataSourceMap.forEach((key, value) -> config.dataSourceMap.put(key, value.clone()));
        return config;
    }

}
