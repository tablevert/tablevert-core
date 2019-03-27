/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tablevert.core.config.Database;
import org.tablevert.core.config.DatabaseType;
import org.tablevert.core.config.DatabaseUser;

class JdbcDatabaseReaderBuilderTest {

    private static final String TESTDB_USER_NAME = "tester";
    private static final String TESTDB_NOUSER_NAME = "nouser";

    private static final String BUILDER_FAILED_MESSAGE = "Builder validation failed";

    @Test
    void succeedsWithValidData() {
        JdbcDatabaseReader.Builder builder = new JdbcDatabaseReader.Builder()
                .forDatabase(prepareTestDatabase())
                .withUser(TESTDB_USER_NAME);
        Assertions.assertDoesNotThrow(() -> builder.build());
    }

    @Test
    void failsWithoutDatabase() {
        JdbcDatabaseReader.Builder builder = new JdbcDatabaseReader.Builder()
                .withUser(TESTDB_USER_NAME);
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("database not specified"));
    }

    @Test
    void failsWithoutUserName() {
        JdbcDatabaseReader.Builder builder = new JdbcDatabaseReader.Builder()
                .forDatabase(prepareTestDatabase());
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("user not specified"));
    }

    @Test
    void failsWithNonDatabaseUser() {
        JdbcDatabaseReader.Builder builder = new JdbcDatabaseReader.Builder()
                .forDatabase(prepareTestDatabase())
                .withUser(TESTDB_NOUSER_NAME);
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("user"));
        Assertions.assertTrue(e.getMessage().contains("not configured for database"));
    }

    private Database prepareTestDatabase() {
        try {
            return new Database.Builder()
                    .forDatabase("dummyDb")
                    .onHost("localhost")
                    .ofType(DatabaseType.POSTGRESQL)
                    .withUser(new DatabaseUser(TESTDB_USER_NAME, "y"))
                    .build();
        } catch (BuilderFailedException e) {
            throw new IllegalStateException("Builder should not fail in test");
        }
    }
}
