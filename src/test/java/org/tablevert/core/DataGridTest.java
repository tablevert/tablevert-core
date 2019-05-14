/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataGridTest {

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

    private static final String BUILDER_FAILED_MESSAGE = "Builder validation failed";
    private static final String GENERATOR_FAILED_MESSAGE = "Failed to write";
    private static final String DUPLICATE_ID_MESSAGE = "Duplicate Id";

    @Test
    void createsDataGridWithValuesFilled() {
        Assertions.assertDoesNotThrow(() -> createSimpleDataGrid());
    }

    @Test
    void createsDataGridWithMissingData() throws Exception {
        Assertions.assertDoesNotThrow(() -> createSimpleDataGridWithMissingData());
    }

    @Test
    void createsDataGridWithoutData() throws Exception {
        Assertions.assertDoesNotThrow(() -> createSimpleDataGridWithoutData());
    }

    @Test
    @Disabled("Enable after DataGrid switch to Builder pattern")
    void failsWithoutColumns() throws Exception {
        createSimpleDataGridWithDuplicateRowId();
        Exception e = Assertions.assertThrows(DataGridException.class,
                () -> createSimpleDataGridWithoutColumns());
        assertTrue(e.getMessage().contains(DUPLICATE_ID_MESSAGE));
    }

    @Test
    void failsWithDuplicateRowId() throws Exception {
        Exception e = Assertions.assertThrows(DataGridException.class,
                () -> createSimpleDataGridWithDuplicateRowId());
        assertTrue(e.getMessage().contains(DUPLICATE_ID_MESSAGE));
        assertTrue(e.getMessage().contains(ROW_ID_00));
    }

    private DataGrid createSimpleDataGrid() throws DataGridException {
        DataGrid dataGrid = new DataGrid();
        addColumns(dataGrid);
        addRows(dataGrid);
        return dataGrid;
    }

    private DataGrid createSimpleDataGridWithDuplicateRowId() throws DataGridException {
        DataGrid dataGrid = new DataGrid();
        addColumns(dataGrid);
        addRowsWithDuplicateId(dataGrid);
        return dataGrid;
    }

    private DataGrid createSimpleDataGridWithMissingData() throws DataGridException {
        DataGrid dataGrid = new DataGrid();
        addColumns(dataGrid);
        addRowsWithMissingData(dataGrid);
        return dataGrid;
    }

    private DataGrid createSimpleDataGridWithoutData() {
        DataGrid dataGrid = new DataGrid();
        addColumns(dataGrid);
        return dataGrid;
    }

    private DataGrid createSimpleDataGridWithoutColumns() throws DataGridException {
        DataGrid dataGrid = new DataGrid();
        addRows(dataGrid);
        return dataGrid;
    }

    private void addColumns(DataGrid dataGrid) {
        dataGrid.addColumn(
                new DataGridColumn(0, COLHEADER_TITLE_00, CLASSNAME_STRING)
        );
        dataGrid.addColumn(
                new DataGridColumn(1, COLHEADER_TITLE_01, CLASSNAME_STRING)
        );
    }


    private void addRows(DataGrid dataGrid) throws DataGridException {
        DataGridRow row = new DataGridRow(ROW_ID_00);
        row.addReplaceValue(0, CELLVALUE_00_00);
        row.addReplaceValue(1, CELLVALUE_00_01);
        dataGrid.addRow(row);
        row = new DataGridRow(ROW_ID_01);
        row.addReplaceValue(0, CELLVALUE_01_00);
        row.addReplaceValue(1, CELLVALUE_01_01);
        dataGrid.addRow(row);
    }

    private void addRowsWithDuplicateId(DataGrid dataGrid) throws DataGridException {
        DataGridRow row = new DataGridRow(ROW_ID_00);
        row.addReplaceValue(0, CELLVALUE_00_00);
        row.addReplaceValue(1, CELLVALUE_00_01);
        dataGrid.addRow(row);
        row = new DataGridRow(ROW_ID_00);
        row.addReplaceValue(0, CELLVALUE_01_00);
        row.addReplaceValue(1, CELLVALUE_01_01);
        dataGrid.addRow(row);
    }

    private void addRowsWithMissingData(DataGrid dataGrid) throws DataGridException {
        DataGridRow row = new DataGridRow(ROW_ID_00);
        row.addReplaceValue(0, CELLVALUE_00_00);
        row.addReplaceValue(1, CELLVALUE_00_01);
        dataGrid.addRow(row);
        row = new DataGridRow(ROW_ID_01);
        row.addReplaceValue(0, CELLVALUE_01_00);
        row.addReplaceValue(1, "");
        dataGrid.addRow(row);
        row = new DataGridRow(ROW_ID_02);
        row.addReplaceValue(0, CELLVALUE_01_00);
        row.addReplaceValue(1, CELLVALUE_01_01);
        dataGrid.addRow(row);
        row = new DataGridRow(ROW_ID_03);
        row.addReplaceValue(0, null);
        row.addReplaceValue(1, CELLVALUE_01_01);
        dataGrid.addRow(row);
        row = new DataGridRow(ROW_ID_04);
        row.addReplaceValue(0, CELLVALUE_01_00);
        row.addReplaceValue(1, CELLVALUE_01_01);
        dataGrid.addRow(row);
    }

}
