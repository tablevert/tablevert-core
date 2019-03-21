/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SimpleTableverterITCase {

    @Test
    @Disabled("Yet incomplete")
    public void readsDataFromPostgres() {
        Tableverter tableverter = new SimpleTableverter(getConfigForPostgresTest());
        Assertions.assertTrue(true);
    }

    private TablevertConfig getConfigForPostgresTest() {
        TablevertConfig config = new SimpleTablevertConfig.Builder()
                .withQuery(new DatabaseQuery(DatabaseType.POSTGRESQL, "testQuery", "SELECT * FROM mydummy;"))
                .build();
        return config;
    }

    private DataGrid fetchPostgresData() throws Exception {
        DatabaseReader dbReader = new JdbcDatabaseReader.Builder(DatabaseType.POSTGRESQL)
                .forHost("localhost")
                .forDatabase("HHSSecondTest")
                .withCredentials("dummyreader", "test")
                .build();
        DatabaseQuery databaseQuery = new DatabaseQuery(DatabaseType.POSTGRESQL, "testQuery", "SELECT * FROM mydummy;");
        return dbReader.fetchData(databaseQuery);
    }
}
