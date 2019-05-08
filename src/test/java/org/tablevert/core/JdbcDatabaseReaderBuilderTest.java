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

class JdbcDatabaseReaderBuilderTest {

    private static final String TESTDB_NAME = "dummyDb";
    private static final String TESTDB_USER_NAME = "tester";

    private static final String TESTQUERY_NAME = "TestQuery";

    private static final String BUILDER_FAILED_MESSAGE = "Builder validation failed";

    @Test
    void succeedsWithValidData() {
        JdbcDatabaseReader.Builder builder = new JdbcDatabaseReader.Builder()
                .usingConfig(createTestConfigComplete())
                .forAppliedQuery(createAppliedQuery());
        Assertions.assertDoesNotThrow(() -> builder.build());
    }

    @Test
    void failsWithoutConfig() {
        JdbcDatabaseReader.Builder builder = new JdbcDatabaseReader.Builder()
                .forAppliedQuery(createAppliedQuery());
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("ablevert configuration not specified"));
    }

    @Test
    void failsWithoutAppliedQuery() {
        JdbcDatabaseReader.Builder builder = new JdbcDatabaseReader.Builder()
                .usingConfig(createTestConfigComplete());
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("applied query not specified"));
    }

    @Test
    void failsForMissingDatabase() {
        JdbcDatabaseReader.Builder builder = new JdbcDatabaseReader.Builder()
                .usingConfig(createTestConfigEmpty())
                .forAppliedQuery(createAppliedQuery());
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("database not configured"));
    }

    @Test
    void failsForMissingDatabaseQuery() {
        JdbcDatabaseReader.Builder builder = new JdbcDatabaseReader.Builder()
                .usingConfig(createTestConfigDatabaseOnly())
                .forAppliedQuery(createAppliedQuery());
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("database not configured"));
    }

    private TablevertConfig createTestConfigComplete() {
        try {
            return new SimpleTablevertConfig.Builder()
                    .withDataSource(createTestDatabase())
                    .withQuery(createTestDatabaseQuery())
                    .build();
        } catch (BuilderFailedException e) {
            throw new IllegalStateException("Builder should not fail in test");
        }
    }

    private TablevertConfig createTestConfigDatabaseOnly() {
        try {
            return new SimpleTablevertConfig.Builder()
                    .withDataSource(createTestDatabase())
                    .build();
        } catch (BuilderFailedException e) {
            throw new IllegalStateException("Builder should not fail in test");
        }
    }

    private TablevertConfig createTestConfigEmpty() {
        try {
            return new SimpleTablevertConfig.Builder()
                    .build();
        } catch (BuilderFailedException e) {
            throw new IllegalStateException("Builder should not fail in test");
        }
    }

    private Database createTestDatabase() {
        try {
            return new Database.Builder()
                    .forDatabase(TESTDB_NAME)
                    .onHost("localhost")
                    .ofType(DatabaseType.POSTGRESQL)
                    .withUser(new BackendUser(TESTDB_USER_NAME, "y"))
                    .build();
        } catch (BuilderFailedException e) {
            throw new IllegalStateException("Builder should not fail in test");
        }
    }

    private PredefinedDatabaseQuery createTestDatabaseQuery() {
        try {
            return new PredefinedDatabaseQuery.Builder()
                    .withName(TESTQUERY_NAME)
                    .accessingDatabase(TESTDB_NAME)
                    .selectingColumns(createQueryColumns())
                    .selectingFrom("nothing")
                    .build();
        } catch (BuilderFailedException e) {
            throw new IllegalStateException("Builder should not fail in test");
        }
    }

    private List<DatabaseQueryColumn> createQueryColumns() {
        List<DatabaseQueryColumn> columns = new ArrayList<>();
        columns.add(new DatabaseQueryColumn("dummyCol"));
        return columns;
    }

    private AppliedQuery createAppliedQuery() {
        return new AppliedDatabaseQuery.Builder()
                .forDatabaseQuery(TESTQUERY_NAME)
                .build();
    }

}
