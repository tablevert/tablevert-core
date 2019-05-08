/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tablevert.core.config.*;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

class DatabaseTableverterITCase {

    private static final String TESTDB_HOST = "localhost";
    private static final String TESTDB_NAME = "HHSSecondTest";

    private static final String TESTQUERY_NAME = "testQuery";
    private static final String TESTQUERY_COLUMN_A_FORMULA = "LEFT(description, 4)";
    private static final String TESTQUERY_COLUMN_A_NAME = "description";
    private static final String TESTQUERY_COLUMN_B_NAME = "id";
    private static final String TESTQUERY_FROM = "mydummy";
    private static final String TESTQUERY_WHERE = "id < 3";

    private static final String TESTUSER_NAME = "dummyreader";


    @Test
    void tablevertsPostgresToXlsx() throws Exception {
        TableverterFactory factory = new TableverterFactory();
        Tableverter tableverter = factory.createDatabaseTableverterFor(getConfigForPostgresTest());
        AppliedDatabaseQuery appliedQuery = new AppliedDatabaseQuery.Builder()
                .forDatabaseQuery(TESTQUERY_NAME)
                .build();
        Output output = tableverter.tablevert(appliedQuery, OutputFormat.XLSX);
        Assertions.assertNotNull(output);

        // TODO: Remove this!!!
        writeOutputToFile(output);

    }

    private TablevertConfig getConfigForPostgresTest() throws Exception {
        TablevertConfig config = new SimpleTablevertConfig.Builder()
                .withDataSource(new Database.Builder()
                        .forDatabase(TESTDB_NAME)
                        .ofType(DatabaseType.POSTGRESQL)
                        .onHost(TESTDB_HOST)
                        .withUser(new BackendUser(TESTUSER_NAME, "test"))
                        .build())
                .withQuery(new PredefinedDatabaseQuery.Builder()
                        .withName(TESTQUERY_NAME)
                        .accessingDatabase(TESTDB_NAME)
                        .selectingColumns(createValidColumns())
                        .selectingFrom(TESTQUERY_FROM)
                        .applyingFilter(TESTQUERY_WHERE)
                        .withSorting(createValidSorting())
                        .build())
                .build();
        return config;
    }

    private List<DatabaseQueryColumn> createValidColumns() {
        List<DatabaseQueryColumn> columns = new ArrayList<>();
        columns.add(new DatabaseQueryColumn(TESTQUERY_COLUMN_A_FORMULA, TESTQUERY_COLUMN_A_NAME));
        columns.add(new DatabaseQueryColumn(TESTQUERY_COLUMN_B_NAME));
        return columns;
    }

    private List<String> createValidSorting() {
        List<String> sorting = new ArrayList<>();
        sorting.add("-" + TESTQUERY_COLUMN_A_NAME);
        return sorting;
    }
    private void writeOutputToFile(Output output) throws Exception {
        if (output == null) {
            return;
        }
        OutputStream outputStream = new FileOutputStream("target/databasetablevertertest_output.xlsx");
        output.writeContent(outputStream);
    }
}
