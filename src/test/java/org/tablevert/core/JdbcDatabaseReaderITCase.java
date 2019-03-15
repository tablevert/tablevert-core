/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JdbcDatabaseReaderITCase {

    @Test
    public void retrievesPostgresData() {
        Assertions.assertDoesNotThrow(() -> fetchPostgresData());
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
