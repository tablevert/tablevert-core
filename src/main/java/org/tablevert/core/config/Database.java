/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

import java.util.Hashtable;
import java.util.Map;

/**
 * A database containing tablevert source data.
 */
public class Database implements Cloneable {

    public static class Builder {
        private DatabaseType dbType;
        private String name;
        private String host;
        private Map<String, DatabaseUser> userMap;

        public Builder() {
            this.userMap = new Hashtable<>();
        }

        public Builder forDatabase(String name) {
            this.name = name;
            return this;
        }

        public Builder ofType(DatabaseType dbType) {
            this.dbType = dbType;
            return this;
        }

        public Builder onHost(String host) {
            this.host = host;
            return this;
        }

        public Builder withUser(DatabaseUser user) {
            String key = user.getName();
            if (this.userMap.containsKey(key)) {
                this.userMap.replace(key, user);
            } else {
                this.userMap.put(key, user);
            }
            return this;
        }

        public Database build() {
            // TODO: Include validation
            Database database = new Database();
            database.dbType = this.dbType;
            database.name = this.name;
            database.host = this.host;
            database.userMap = this.userMap;
            return database;
        }

    }

    private DatabaseType dbType;
    private String name;
    private String host;
    private Map<String, DatabaseUser> userMap;

    private Database() {
    }

//    public TablevertSourceType getSourceType() {
//        return TablevertSourceType.DATABASE;
//    }

    public DatabaseType getDbType() {
        return dbType;
    }

    public String getHost() {
        return host;
    }

    public String getName() {
        return name;
    }

    public String getUserSecret(String userName) {
        if (!userMap.containsKey(userName)) {
            return null;
        }
        return userMap.get(userName).getSecret();
    }

    public Database clone() {
        Builder cloneBuilder = new Builder()
                .forDatabase(name)
                .ofType(dbType)
                .onHost(host);
        this.userMap.entrySet().stream().forEach(user -> cloneBuilder.withUser(user.getValue().clone()));
        return cloneBuilder.build();
    }
}
