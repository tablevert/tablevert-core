/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tablevert.core.config.Database;
import org.tablevert.core.config.DatabaseQuery;
import org.tablevert.core.config.DatabaseType;
import org.tablevert.core.config.DatabaseUser;

public class JdbcDatabaseReaderITCase {

    private static final String DBQUERY_SIMPLE = "SELECT * FROM mydummy;";

    @Test
    public void retrievesPostgresData() {
        Assertions.assertDoesNotThrow(() -> fetchPostgresData());
    }

    private DataGrid fetchPostgresData() throws Exception {
        DatabaseReader dbReader = new JdbcDatabaseReader.Builder()
                .forDatabase(new Database.Builder()
                        .forDatabase("HHSSecondTest")
                        .ofType(DatabaseType.POSTGRESQL)
                        .onHost("localhost")
                        .withUser(new DatabaseUser("dummyreader", "test")).build())
                .withUser("dummyreader")
                .build();
        DatabaseQuery databaseQuery = new DatabaseQuery.Builder()
                .withName("testQuery")
                .accessingDatabase("HHSSecondTest")
                .withStatement(DBQUERY_SIMPLE)
                .build();
        return dbReader.fetchData(DBQUERY_SIMPLE);
    }

}
