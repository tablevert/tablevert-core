/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tablevert.core.BuilderFailedException;

class DatabaseBuilderTest {

    private static final String TESTDB_NAME = "dummy";
    private static final String TESTDB_HOST = "localhost";
    private static final Integer TESTDB_PORT = 4711;
    private static final String TESTDB_USER_NAME = "tester";
    private static final String TESTDB_USER_SECRET = "test";

    private static final String BUILDER_FAILED_MESSAGE = "Builder validation failed";

    @Test
    void succeedsForValidData() {
        Database.Builder builder = new Database.Builder()
                .forDatabase(TESTDB_NAME)
                .ofType(DatabaseType.POSTGRESQL)
                .onHost(TESTDB_HOST)
                .withPort(TESTDB_PORT)
                .withUser(new BackendUser(TESTDB_USER_NAME, TESTDB_USER_SECRET));
        Assertions.assertDoesNotThrow(() -> builder.build());
    }

    @Test
    void succeedsForMissingPort() {
        Database.Builder builder = new Database.Builder()
                .forDatabase(TESTDB_NAME)
                .ofType(DatabaseType.POSTGRESQL)
                .onHost(TESTDB_HOST)
                .withUser(new BackendUser(TESTDB_USER_NAME, TESTDB_USER_SECRET));
        Assertions.assertDoesNotThrow(() -> builder.build());
    }

    @Test
    void failsOnMissingDatabase() {
        Database.Builder builder = new Database.Builder()
                .ofType(DatabaseType.POSTGRESQL)
                .onHost(TESTDB_HOST)
                .withPort(TESTDB_PORT)
                .withUser(new BackendUser(TESTDB_USER_NAME, TESTDB_USER_SECRET));
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("database name not specified"));
    }

    @Test
    void failsOnMissingType() {
        Database.Builder builder = new Database.Builder()
                .forDatabase(TESTDB_NAME)
                .onHost(TESTDB_HOST)
                .withPort(TESTDB_PORT)
                .withUser(new BackendUser(TESTDB_USER_NAME, TESTDB_USER_SECRET));
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("database type not specified"));
    }

    @Test
    void failsOnMissingHostName() {
        Database.Builder builder = new Database.Builder()
                .forDatabase(TESTDB_NAME)
                .ofType(DatabaseType.POSTGRESQL)
                .withPort(TESTDB_PORT)
                .withUser(new BackendUser(TESTDB_USER_NAME, TESTDB_USER_SECRET));
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("host not specified"));
    }

    @Test
    void failsOnMissingUsers() {
        Database.Builder builder = new Database.Builder()
                .forDatabase(TESTDB_NAME)
                .ofType(DatabaseType.POSTGRESQL)
                .onHost(TESTDB_HOST)
                .withPort(TESTDB_PORT);
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("no users specified"));
    }

}
