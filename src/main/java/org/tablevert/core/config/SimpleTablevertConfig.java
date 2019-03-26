/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

import javax.xml.crypto.Data;
import java.util.Hashtable;
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
            String key = database.getName();
            if (databaseMap.containsKey(key)) {
                databaseMap.replace(key, database);
            } else {
                databaseMap.put(key, database);
            }
            return this;
        }

        public Builder withQuery(DatabaseQuery query) {
            String key = query.getName();
            if (queryMap.containsKey(key)) {
                queryMap.replace(key, query);
            } else {
                queryMap.put(key, query);
            }
            return this;
        }

        public Builder withUser(DatabaseUser user) {
            String key = user.getName();
            if (userMap.containsKey(key)) {
                userMap.replace(key, user);
            } else {
                userMap.put(key, user);
            }
            return this;
        }

        public SimpleTablevertConfig build() {
            SimpleTablevertConfig config = new SimpleTablevertConfig();
            config.queryMap = this.queryMap;
            config.databaseMap = this.databaseMap;
            return config;
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

    public DatabaseType getDatabaseTypeForQuery(String queryName) {
        DatabaseQuery databaseQuery = queryMap.containsKey(queryName) ? queryMap.get(queryName) : null;
        return (databaseQuery == null || !databaseMap.containsKey(databaseQuery.getDatabaseName()))
                ? null : databaseMap.get(databaseQuery.getDatabaseName()).getDbType();
    }

    public String getHostNameForDatabaseQuery(String queryName) {
        DatabaseQuery databaseQuery = queryMap.containsKey(queryName) ? queryMap.get(queryName) : null;
        return (databaseQuery == null || !databaseMap.containsKey(databaseQuery.getDatabaseName()))
                ? null : databaseMap.get(databaseQuery.getDatabaseName()).getHost();
    }
}
