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

public class JdbcDatabaseReaderBuilderTest {

    @Test
    public void failsWithoutDatabase() {
        JdbcDatabaseReader.Builder builder = new JdbcDatabaseReader.Builder()
                .withUser("x");
        Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build(), "builder validation failed");
    }

    @Test
    public void knowsPostgresDriver() {
        JdbcDatabaseReader.Builder builder = new JdbcDatabaseReader.Builder()
                .forDatabase(prepareTestDatabase())
                .withUser("x");
        Assertions.assertDoesNotThrow(() -> builder.build());
    }

    private Database prepareTestDatabase() {
        return new Database.Builder()
                .forDatabase("dummyDb")
                .ofType(DatabaseType.POSTGRESQL)
                .withUser(new DatabaseUser("x", "y"))
                .build();
    }
}
