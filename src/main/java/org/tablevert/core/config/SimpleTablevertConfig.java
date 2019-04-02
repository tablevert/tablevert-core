/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

import javax.xml.crypto.Data;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Simple implementation of the {@link TablevertConfig} interface.
 */
public class SimpleTablevertConfig implements TablevertConfig {

    /**
     * Builder for SimpleTablevertConfig instances.
     */
    public static class Builder {

        private Map<String, Database> databaseMap;
        private final Map<String, DatabaseQuery> queryMap;
        private Map<String, DatabaseUser> userMap;

        public Builder() {
            this.queryMap = new Hashtable<>();
            this.databaseMap = new Hashtable<>();
            this.userMap = new Hashtable<>();
        }

        public Builder withDatabase(Database database) {
            putOrReplace(databaseMap, database.getName(), database);
            return this;
        }

        public Builder withDatabases(List<Database> databases) {
            if (databases != null) {
                for (Database database : databases) {
                    withDatabase(database);
                }
            }
            return this;
        }

        public Builder withQuery(DatabaseQuery query) {
            putOrReplace(queryMap, query.getName(), query);
            return this;
        }

        public Builder withQueries(List<DatabaseQuery> queries) {
            if (queries != null) {
                for (DatabaseQuery query : queries) {
                    withQuery(query);
                }
            }
            return this;
        }

        public SimpleTablevertConfig build() {
            SimpleTablevertConfig config = new SimpleTablevertConfig();
            config.queryMap = this.queryMap;
            config.databaseMap = this.databaseMap;
            return config;
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

}
