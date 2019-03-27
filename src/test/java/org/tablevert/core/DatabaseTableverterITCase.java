/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tablevert.core.config.*;

import java.io.FileOutputStream;
import java.io.OutputStream;

class DatabaseTableverterITCase {

    private static final String TESTDB_HOST = "localhost";
    private static final String TESTDB_NAME = "HHSSecondTest";
    private static final String TESTQUERY_NAME = "testQuery";
    private static final String TESTUSER_NAME = "dummyreader";


    @Test
    void tablevertsPostgresToXlsx() throws Exception {
        Tableverter tableverter = new DatabaseTableverter(getConfigForPostgresTest());
        AppliedDatabaseQuery appliedQuery = new AppliedDatabaseQuery.Builder()
                .forDatabaseQuery(TESTQUERY_NAME)
                .withUser(TESTUSER_NAME)
                .build();
        Output output = tableverter.tablevert(appliedQuery, OutputFormat.XLSX);
        Assertions.assertNotNull(output);

        // TODO: Remove this!!!
        writeOutputToFile(output);

    }

    private TablevertConfig getConfigForPostgresTest() throws Exception {
        TablevertConfig config = new SimpleTablevertConfig.Builder()
                .withDatabase(new Database.Builder()
                        .forDatabase(TESTDB_NAME)
                        .ofType(DatabaseType.POSTGRESQL)
                        .onHost(TESTDB_HOST)
                        .withUser(new DatabaseUser(TESTUSER_NAME, "test"))
                        .build())
                .withQuery(new DatabaseQuery.Builder()
                        .withName(TESTQUERY_NAME)
                        .accessingDatabase(TESTDB_NAME)
                        .withStatement("SELECT * FROM mydummy;")
                        .build())
                .withUser(new DatabaseUser(TESTUSER_NAME, "test"))
                .build();
        return config;
    }

    private void writeOutputToFile(Output output) throws Exception {
        if (output == null) {
            return;
        }
        OutputStream outputStream = new FileOutputStream("target/simpletablevertertest_output.xlsx");
        output.writeContent(outputStream);
    }
}
