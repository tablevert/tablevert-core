/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HtmlOutputGeneratorTest {

    private static final String CLASSNAME_STRING = "java.lang.String";

    private static final String COLHEADER_TITLE_00 = "A";
    private static final String COLHEADER_TITLE_01 = "B";

    private static final String CELLVALUE_00_00 = "value (0, 0)";
    private static final String CELLVALUE_00_01 = "value (0, 1)";
    private static final String CELLVALUE_01_00 = "value (1, 0)";
    private static final String CELLVALUE_01_01 = "value (1, 1)";

    private static final String BUILDER_FAILED_MESSAGE = "Builder validation failed";
    private static final String GENERATOR_FAILED_MESSAGE = "Failed to write";

    @Test
    void createsValidHtmlOutput() throws Exception {
        DataGrid dataGrid = createSimpleDataGrid();
        OutputGenerator outputGenerator = new HtmlOutputGenerator();

        Output output = outputGenerator.process(dataGrid);

        assertNotNull(output);
        assertEquals(expectedResult(), output.toString());
    }

    @Test
    void createsHtmlOutputWithNoData() throws Exception {
        DataGrid dataGrid = createSimpleDataGridWithoutData();
        OutputGenerator outputGenerator = new HtmlOutputGenerator();

        Output output = outputGenerator.process(dataGrid);

        assertNotNull(output);
        assertEquals(expectedResultWithoutData(), output.toString());
    }

    @Test
    void failsWithoutColumns() {
        DataGrid dataGrid = createSimpleDataGridWithoutColumns();
        OutputGenerator outputGenerator = new HtmlOutputGenerator();

        Exception e = Assertions.assertThrows(OutputGeneratorException.class,
                () -> outputGenerator.process(dataGrid));
        assertTrue(e.getMessage().contains(GENERATOR_FAILED_MESSAGE));
        Throwable cause = e.getCause();
        assertNotNull(cause);
        assertEquals(BuilderFailedException.class, cause.getClass());
        assertTrue(cause.getMessage().contains(BUILDER_FAILED_MESSAGE));
        assertTrue(cause.getMessage().contains("columns empty"));
    }

    private DataGrid createSimpleDataGrid() {
        DataGrid dataGrid = new DataGrid();
        addColumns(dataGrid);
        addRows(dataGrid);
        return dataGrid;
    }

    private DataGrid createSimpleDataGridWithoutData() {
        DataGrid dataGrid = new DataGrid();
        addColumns(dataGrid);
        return dataGrid;
    }

    private DataGrid createSimpleDataGridWithoutColumns() {
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


    private void addRows(DataGrid dataGrid) {
        DataGridRow row = new DataGridRow(0);
        row.addReplaceValue(0, CELLVALUE_00_00);
        row.addReplaceValue(1, CELLVALUE_00_01);
        dataGrid.addRow(row);
        row = new DataGridRow(1);
        row.addReplaceValue(0, CELLVALUE_01_00);
        row.addReplaceValue(1, CELLVALUE_01_01);
        dataGrid.addRow(row);
    }

    private String expectedResult() {
        return new StringBuilder()
                .append("<table>")
                .append("<colgroup>")
                .append("<col></col>")
                .append("<col></col>")
                .append("</colgroup>")
                .append("<thead>")
                .append("<tr>")
                .append("<th>").append(COLHEADER_TITLE_00).append("</th>")
                .append("<th>").append(COLHEADER_TITLE_01).append("</th>")
                .append("</tr>")
                .append("</thead>")
                .append("<tbody>")
                .append("<tr id=\"tvdat-0\">")
                .append("<td>").append(CELLVALUE_00_00).append("</td>")
                .append("<td>").append(CELLVALUE_00_01).append("</td>")
                .append("</tr>")
                .append("<tr id=\"tvdat-1\">")
                .append("<td>").append(CELLVALUE_01_00).append("</td>")
                .append("<td>").append(CELLVALUE_01_01).append("</td>")
                .append("</tr>")
                .append("</tbody>")
                .append("</table>")
                .toString();
    }

    private String expectedResultWithoutData() {
        return new StringBuilder()
                .append("<table>")
                .append("<colgroup>")
                .append("<col></col>")
                .append("<col></col>")
                .append("</colgroup>")
                .append("<thead>")
                .append("<tr>")
                .append("<th>").append(COLHEADER_TITLE_00).append("</th>")
                .append("<th>").append(COLHEADER_TITLE_01).append("</th>")
                .append("</tr>")
                .append("</thead>")
                .append("<tbody>")
                .append("</tbody>")
                .append("</table>")
                .toString();
    }

}
