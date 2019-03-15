/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * Query for retrieving data using a {@DatabaseReader} instance.
 */
class DatabaseQuery {
    private DatabaseType dbType;
    private String name;
    private String query;

    DatabaseQuery(DatabaseType dbType, String name, String query) {
        this.dbType = dbType;
        this.name = name;
        this.query = query;
    }

    String getName() {
        return name;
    }

    String getQuery() {
        return query;
    }

}
