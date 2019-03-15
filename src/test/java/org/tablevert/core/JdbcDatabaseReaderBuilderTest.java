/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JdbcDatabaseReaderBuilderTest {

    @Test
    public void failsWithoutHostName() {
        JdbcDatabaseReader.Builder builder = new JdbcDatabaseReader.Builder(DatabaseType.POSTGRESQL)
                .forDatabase("dummyDb")
                .withCredentials("x", "y");
        Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build(), "builder validation failed");
    }

    @Test
    public void failsWithoutDatabaseName() {
        JdbcDatabaseReader.Builder builder = new JdbcDatabaseReader.Builder(DatabaseType.POSTGRESQL)
                .forHost("localhost")
                .withCredentials("x", "y");
        Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build(), "builder validation failed");
    }

    @Test
    public void failsWithoutCredentials() {
        JdbcDatabaseReader.Builder builder = new JdbcDatabaseReader.Builder(DatabaseType.POSTGRESQL)
                .forHost("localhost")
                .forDatabase("dummyDb");
        Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build(), "builder validation failed");
    }

    @Test
    public void knowsPostgresDriver() {
        JdbcDatabaseReader.Builder builder = new JdbcDatabaseReader.Builder(DatabaseType.POSTGRESQL)
                .forHost("localhost")
                .forDatabase("dummyDb")
                .withCredentials("x", "y");
        Assertions.assertDoesNotThrow(() -> builder.build());
    }
}
