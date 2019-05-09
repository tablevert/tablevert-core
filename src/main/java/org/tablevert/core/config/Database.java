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
 * A database containing tablevert source data.
 */
public class Database implements DataSource, Cloneable {

    public static class Builder {
        private static final int DEFAULT_PORT_POSTGRESQL = 5432;

        private DatabaseType dbType;
        private String name;
        private String host;
        private Integer port;
        private Map<String, BackendUser> userMap;

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

        public Builder withPort(Integer port) {
            this.port = port;
            return this;
        }

        public Builder withUser(BackendUser user) {
            String key = user.getName();
            if (this.userMap.containsKey(key)) {
                this.userMap.replace(key, user);
            } else {
                this.userMap.put(key, user);
            }
            return this;
        }

        public Builder withUsers(List<BackendUser> users) {
            if (users != null) {
                for (BackendUser user : users) {
                    withUser(user);
                }
            }
            return this;
        }

        public Database build() throws BuilderFailedException {
            checkInitPort();
            validate();
            Database database = new Database();
            database.dbType = this.dbType;
            database.name = this.name;
            database.host = this.host;
            database.port = this.port;
            database.userMap = this.userMap;
            return database;
        }

        private void checkInitPort() throws BuilderFailedException {
            if (port != null || dbType == null) {
                return;
            }
            switch (dbType) {
                case POSTGRESQL:
                    port = DEFAULT_PORT_POSTGRESQL;
                    return;
                default:
                    throw new BuilderFailedException("No default port available for database type ["
                            + dbType.name() + "]");
            }
        }

        private void validate() throws BuilderFailedException {
            String errors = "";
            if (name == null || name.isEmpty()) {
                errors += "- database name not specified";
            }
            if (dbType == null) {
                errors += "- database type not specified";
            }
            if (host == null || host.isEmpty()) {
                errors += "- host not specified";
            }
            if (port == null) {
                errors += "- port not specified";
            }
            if (userMap == null || userMap.isEmpty()) {
                errors += "- no users specified";
            }
            if (!errors.isEmpty()) {
                throw new BuilderFailedException("Builder validation failed with errors: " + errors);
            }
        }

    }

    private DatabaseType dbType;
    private String name;
    private String host;
    private Integer port;
    private Map<String, BackendUser> userMap;

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

    public Integer getPort() { return port; }

    public String getDefaultUserName() {
        // TODO: Add default user property or enhanced user selection logic!
        if (userMap.isEmpty()) {
            return null;
        }
        return userMap.get(userMap.keySet().stream().findFirst().get()).getName();
    }

    public String getUserSecret(String userName) {
        if (!userMap.containsKey(userName)) {
            return null;
        }
        return userMap.get(userName).getSecret();
    }

    @Override
    public Database clone() {
        try {
            Builder cloneBuilder = new Builder()
                    .forDatabase(name)
                    .ofType(dbType)
                    .onHost(host)
                    .withPort(port);
            this.userMap.entrySet().forEach(user -> cloneBuilder.withUser(user.getValue().clone()));
            return cloneBuilder.build();
        } catch (BuilderFailedException e) {
            throw new IllegalStateException("Builder should never fail in clone()");
        }
    }
}
