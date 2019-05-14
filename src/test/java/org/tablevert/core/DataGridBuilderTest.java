/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataGridBuilderTest {

    private static final String CLASSNAME_STRING = "java.lang.String";

    private static final String COLHEADER_TITLE_00 = "A";
    private static final String COLHEADER_TITLE_01 = "B";

    private static final String ROW_ID_00 = "0";
    private static final String ROW_ID_01 = "1";
    private static final String ROW_ID_02 = "2";
    private static final String ROW_ID_03 = "3";
    private static final String ROW_ID_04 = "4";

    private static final String CELLVALUE_00_00 = "value (0, 0)";
    private static final String CELLVALUE_00_01 = "value (0, 1)";
    private static final String CELLVALUE_01_00 = "value (1, 0)";
    private static final String CELLVALUE_01_01 = "value (1, 1)";

    private static final String MESSAGE_VALIDATION_FAILED = "DataGrid validation failed";
    private static final String MESSAGE_NO_COLUMNS = "Data section must not be started with without columns defined";
    private static final String MESSAGE_DUPLICATE_ID = "Duplicate Id";

    @Test
    void createsDataGridWithValuesFilled() throws Exception {
        DataGrid.Builder builder = new DataGrid.Builder();
        addColumnsTo(builder);
        builder.andData();
        addRowsTo(builder);
        Assertions.assertDoesNotThrow(() -> builder.build());
    }

    @Test
    void createsDataGridWithMissingData() throws Exception {
        DataGrid.Builder builder = new DataGrid.Builder();
        addColumnsTo(builder);
        builder.andData();
        addRowsWithMissingDataTo(builder);
        Assertions.assertDoesNotThrow(() -> builder.build());
    }

    @Test
    void createsDataGridWithoutData() throws Exception {
        DataGrid.Builder builder = new DataGrid.Builder();
        addColumnsTo(builder);
        Assertions.assertDoesNotThrow(() -> builder.build());
    }

    @Test
    void failsWithoutAnything() throws Exception {
        DataGrid.Builder builder = new DataGrid.Builder();
        Exception e = Assertions.assertThrows(DataGridException.class, () -> builder.build());
        assertTrue(e.getMessage().contains(MESSAGE_VALIDATION_FAILED));
    }

    @Test
    void failsWithoutColumns() throws Exception {
        DataGrid.Builder builder = new DataGrid.Builder();
        Exception e = Assertions.assertThrows(DataGridException.class, () -> builder.andData());
        assertTrue(e.getMessage().contains(MESSAGE_NO_COLUMNS));
    }

    @Test
    void failsWithDuplicateRowId() throws Exception {
        DataGrid.Builder builder = new DataGrid.Builder();
        addColumnsTo(builder);
        builder.andData();
        Exception e = Assertions.assertThrows(DataGridException.class,
                () -> addRowsWithDuplicateIdTo(builder));
        assertTrue(e.getMessage().contains(MESSAGE_DUPLICATE_ID));
        assertTrue(e.getMessage().contains(ROW_ID_00));
    }

    private void addColumnsTo(DataGrid.Builder dataGridBuilder) throws DataGridException {
        dataGridBuilder.withColumn(
                new DataGridColumn(0, COLHEADER_TITLE_00, CLASSNAME_STRING)
        );
        dataGridBuilder.withColumn(
                new DataGridColumn(1, COLHEADER_TITLE_01, CLASSNAME_STRING)
        );
    }


    private void addRowsTo(DataGrid.Builder dataGridBuilder) throws DataGridException {
        DataGridRow row = new DataGridRow(ROW_ID_00);
        row.addReplaceValue(0, CELLVALUE_00_00);
        row.addReplaceValue(1, CELLVALUE_00_01);
        dataGridBuilder.withRow(row);
        row = new DataGridRow(ROW_ID_01);
        row.addReplaceValue(0, CELLVALUE_01_00);
        row.addReplaceValue(1, CELLVALUE_01_01);
        dataGridBuilder.withRow(row);
    }

    private void addRowsWithDuplicateIdTo(DataGrid.Builder dataGridBuilder) throws DataGridException {
        DataGridRow row = new DataGridRow(ROW_ID_00);
        row.addReplaceValue(0, CELLVALUE_00_00);
        row.addReplaceValue(1, CELLVALUE_00_01);
        dataGridBuilder.withRow(row);
        row = new DataGridRow(ROW_ID_00);
        row.addReplaceValue(0, CELLVALUE_01_00);
        row.addReplaceValue(1, CELLVALUE_01_01);
        dataGridBuilder.withRow(row);
    }

    private void addRowsWithMissingDataTo(DataGrid.Builder dataGridBuilder) throws DataGridException {
        DataGridRow row = new DataGridRow(ROW_ID_00);
        row.addReplaceValue(0, CELLVALUE_00_00);
        row.addReplaceValue(1, CELLVALUE_00_01);
        dataGridBuilder.withRow(row);
        row = new DataGridRow(ROW_ID_01);
        row.addReplaceValue(0, CELLVALUE_01_00);
        row.addReplaceValue(1, "");
        dataGridBuilder.withRow(row);
        row = new DataGridRow(ROW_ID_02);
        row.addReplaceValue(0, CELLVALUE_01_00);
        row.addReplaceValue(1, CELLVALUE_01_01);
        dataGridBuilder.withRow(row);
        row = new DataGridRow(ROW_ID_03);
        row.addReplaceValue(0, null);
        row.addReplaceValue(1, CELLVALUE_01_01);
        dataGridBuilder.withRow(row);
        row = new DataGridRow(ROW_ID_04);
        row.addReplaceValue(0, CELLVALUE_01_00);
        row.addReplaceValue(1, CELLVALUE_01_01);
        dataGridBuilder.withRow(row);
    }

}
