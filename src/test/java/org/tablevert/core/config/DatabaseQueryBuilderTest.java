/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tablevert.core.BuilderFailedException;

import java.util.ArrayList;
import java.util.List;

class DatabaseQueryBuilderTest {

    private static final String QUERY_NAME = "testQuery";
    private static final String QUERY_COLUMN_A_FORMULA = "LEFT(dummycol, 4)";
    private static final String QUERY_COLUMN_A_NAME_VALID = "col_a";
    private static final String QUERY_COLUMN_A_NAME_INVALID = "col_a invalid";
    private static final String QUERY_COLUMN_B_NAME_VALID = "col_b";
    private static final String QUERY_FROM = "dummytable";
    private static final String QUERY_WHERE = "dummycol='affe'";
    private static final String TESTDB_NAME = "dummy";

    private static final String BUILDER_FAILED_MESSAGE = "Builder validation failed";

    @Test
    void succeedsForValidData() {
        DatabaseQuery.Builder builder = new DatabaseQuery.Builder()
                .withName(QUERY_NAME)
                .accessingDatabase(TESTDB_NAME)
                .selectingColumns(createValidColumns())
                .selectingFrom(QUERY_FROM)
                .applyingFilter(QUERY_WHERE)
                .withSorting(createValidSorting());
        Assertions.assertDoesNotThrow(() -> builder.build());
    }

    @Test
    void failsOnMissingName() {
        DatabaseQuery.Builder builder = new DatabaseQuery.Builder()
                .accessingDatabase(TESTDB_NAME)
                .selectingColumns(createValidColumns())
                .selectingFrom(QUERY_FROM)
                .applyingFilter(QUERY_WHERE)
                .withSorting(createValidSorting());
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("query name not specified"));
    }

    @Test
    void failsOnMissingDatabaseName() {
        DatabaseQuery.Builder builder = new DatabaseQuery.Builder()
                .withName(QUERY_NAME)
                .selectingColumns(createValidColumns())
                .selectingFrom(QUERY_FROM)
                .applyingFilter(QUERY_WHERE)
                .withSorting(createValidSorting());
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("database name not specified"));
    }

    @Test
    void failsOnMissingFromClause() {
        DatabaseQuery.Builder builder = new DatabaseQuery.Builder()
                .withName(QUERY_NAME)
                .accessingDatabase(TESTDB_NAME)
                .selectingColumns(createValidColumns())
                .applyingFilter(QUERY_WHERE)
                .withSorting(createValidSorting());
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("from clause not specified"));
    }

    @Test
    void failsOnMissingColumns() {
        DatabaseQuery.Builder builder = new DatabaseQuery.Builder()
                .withName(QUERY_NAME)
                .accessingDatabase(TESTDB_NAME)
                .applyingFilter(QUERY_WHERE)
                .withSorting(createValidSorting());
        Exception e = Assertions.assertThrows(BuilderFailedException.class,
                () -> builder.build());
        Assertions.assertTrue(e.getMessage().contains(BUILDER_FAILED_MESSAGE));
        Assertions.assertTrue(e.getMessage().contains("columns not specified"));
    }

    private List<DatabaseQueryColumn> createValidColumns() {
        List<DatabaseQueryColumn> columns = new ArrayList<>();
        columns.add(new DatabaseQueryColumn(QUERY_COLUMN_A_FORMULA, QUERY_COLUMN_A_NAME_VALID));
        columns.add(new DatabaseQueryColumn(QUERY_COLUMN_B_NAME_VALID));
        return columns;
    }

    private List<String> createValidSorting() {
        List<String> sorting = new ArrayList<>();
        sorting.add(QUERY_COLUMN_B_NAME_VALID);
        sorting.add("-" + QUERY_COLUMN_A_NAME_VALID);
        return sorting;
    }


}
