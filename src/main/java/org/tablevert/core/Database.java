/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

class Database implements TablevertSource {
    private DatabaseType dbType;

    Database(DatabaseType dbType) {
        this.dbType = dbType;
    }

    public TablevertSourceType getSourceType() {
        return TablevertSourceType.DATABASE;
    }

    public DataGrid fetchData(Object fetchInstruction) throws Exception {
        DatabaseQuery query = (DatabaseQuery) fetchInstruction;
        DatabaseReader databaseReader;
        switch (dbType) {
            case POSTGRESQL:
                databaseReader = new JdbcDatabaseReader.Builder(dbType).build();
                break;
            default:
                throw new IllegalStateException("fetchData not defined for database type " + dbType);
        }
        return databaseReader.fetchData(query);
    }

}
