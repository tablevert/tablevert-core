/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.tablevert.core.config.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

@Testcontainers
class JdbcDatabaseReaderContainerITCase {

    private static final String TESTDB_NAME = "PostgresTest";
    private static final String TESTDB_HOST = "localhost";
    private static final String TESTDB_USER_NAME = "tester";
    private static final String TESTDB_USER_PW = "test";
    private static final String TESTDB_INIT_SCRIPT_PATH = "org/tablevert/core/DbInitForJdbcDatabaseReaderITCase.sql";

    private static final String TESTQUERY_NAME = "TestQuery";
    private static final String TESTQUERY_COLNAME_A = "id";
    private static final String TESTQUERY_FROM = "mydummy";


    @Container
    private static final PostgreSQLContainer POSTGRE_SQL_CONTAINER = (PostgreSQLContainer) new PostgreSQLContainer()
            .withDatabaseName(TESTDB_NAME)
            .withUsername(TESTDB_USER_NAME)
            .withPassword(TESTDB_USER_PW)
            .withInitScript(TESTDB_INIT_SCRIPT_PATH);

    @Test
    void retrievesPostgresData() {
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
                .withPort(POSTGRE_SQL_CONTAINER.getFirstMappedPort())
                .withUser(new BackendUser(TESTDB_USER_NAME, TESTDB_USER_PW))
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
