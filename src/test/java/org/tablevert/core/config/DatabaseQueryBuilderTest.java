/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tablevert.core.BuilderFailedException;

class DatabaseQueryBuilderTest {

    private static final String QUERY_NAME = "testQuery";
    private static final String QUERY_STATEMENT = "SELECT * FROM dummytable;";
    private static final String QUERY_ILLEGAL_STATEMENT = "INSERT INTO dummytable (a) VALUES ('xxx');";
    private static final String TESTDB_NAME = "dummy";

    private static final String BUILDER_FAILED_MESSAGE = "Builder validation failed";

    @Test
    void succeedsForValidData() {
        DatabaseQuery.Builder builder = new DatabaseQuery.Builder()
                .withName(QUERY_NAME)
                .accessingDatabase(TESTDB_NAME)
                .withStatement(QUERY_STATEMENT);
        Assertions.assertDoesNotThrow(() -> builder.build());
    }

    @Test
    void failsOnMissingName() {
        DatabaseQuery.Builder builder = new DatabaseQuery.Builder()
                .accessingDatabase(TESTDB_NAME)
                .withStatement(QUERY_STATEMENT);
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("query name not specified"));
    }

    @Test
    void failsOnMissingDatabaseName() {
        DatabaseQuery.Builder builder = new DatabaseQuery.Builder()
                .withName(QUERY_NAME)
                .withStatement(QUERY_STATEMENT);
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("database name not specified"));
    }

    @Test
    void failsOnMissingQueryStatement() {
        DatabaseQuery.Builder builder = new DatabaseQuery.Builder()
                .withName(QUERY_NAME)
                .accessingDatabase(TESTDB_NAME);
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("query statement not specified"));
    }

    @Test
    void failsOnIllegalQueryStatement() {
        DatabaseQuery.Builder builder = new DatabaseQuery.Builder()
                .withName(QUERY_NAME)
                .accessingDatabase(TESTDB_NAME)
                .withStatement(QUERY_ILLEGAL_STATEMENT);
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("query statement"));
        Assertions.assertTrue(e.getMessage().contains("does not start with 'select'"));
    }


}
