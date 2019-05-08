/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tablevert.core.config.*;

import java.util.ArrayList;
import java.util.List;

public class JdbcDatabaseReaderITCase {

    private static final String TESTDB_NAME = "HHSSecondTest";
    private static final String TESTDB_HOST = "localhost";
    private static final String TESTDB_USER_NAME = "dummyreader";

    private static final String TESTQUERY_NAME = "TestQuery";
    private static final String TESTQUERY_COLNAME_A = "id";
    private static final String TESTQUERY_FROM = "mydummy";

    @Test
    public void retrievesPostgresData() {
        // TODO: Switch to container-based testing!!!
        Assertions.assertDoesNotThrow(() -> fetchPostgresData());
    }

    private DataGrid fetchPostgresData() throws Exception {
        DatabaseReader dbReader = new JdbcDatabaseReader.Builder()
                .usingConfig(createTestConfig())
                .forAppliedQuery(createAppliedQuery())
                .build();
        return dbReader.read();
    }

    private TablevertConfig createTestConfig() throws Exception {
        return new SimpleTablevertConfig.Builder()
                .withDataSource(createTestDatabase())
                .withQuery(createTestDatabaseQuery())
                .build();
    }

    private Database createTestDatabase() throws Exception {
        return new Database.Builder()
                .forDatabase(TESTDB_NAME)
                .ofType(DatabaseType.POSTGRESQL)
                .onHost(TESTDB_HOST)
                .withUser(new BackendUser(TESTDB_USER_NAME, "teest"))
                .build();
    }

    private PredefinedDatabaseQuery createTestDatabaseQuery() throws Exception {
        return new PredefinedDatabaseQuery.Builder()
                .withName(TESTQUERY_NAME)
                .accessingDatabase(TESTDB_NAME)
                .selectingColumns(createQueryColumns())
                .selectingFrom(TESTQUERY_FROM)
                .build();
    }

    private List<DatabaseQueryColumn> createQueryColumns() {
        List<DatabaseQueryColumn> columns = new ArrayList<>();
        columns.add(new DatabaseQueryColumn(TESTQUERY_COLNAME_A));
        return columns;
    }

    private AppliedQuery createAppliedQuery() {
        return new AppliedDatabaseQuery.Builder()
                .forDatabaseQuery(TESTQUERY_NAME)
                .build();
    }

}
