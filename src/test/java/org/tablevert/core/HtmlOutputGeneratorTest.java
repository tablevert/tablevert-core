/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HtmlOutputGeneratorTest {

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

    @Test
    void createsHtmlOutputWithAllCellsFilled() throws Exception {
        DataGrid dataGrid = createSimpleDataGrid();
        OutputGenerator outputGenerator = new HtmlOutputGenerator();

        Output output = outputGenerator.process(dataGrid);

        assertNotNull(output);
        assertEquals(expectedResult(), output.toString());
    }

    @Test
    void createsHtmlOutputWithMissingData() throws Exception {
        DataGrid dataGrid = createSimpleDataGridWithMissingData();
        OutputGenerator outputGenerator = new HtmlOutputGenerator();

        Output output = outputGenerator.process(dataGrid);

        assertNotNull(output);
        assertEquals(expectedResultWithMissingData(), output.toString());
    }

    @Test
    void createsHtmlOutputWithNoData() throws Exception {
        DataGrid dataGrid = createSimpleDataGridWithoutData();
        OutputGenerator outputGenerator = new HtmlOutputGenerator();

        Output output = outputGenerator.process(dataGrid);

        assertNotNull(output);
        assertEquals(expectedResultWithoutData(), output.toString());
    }

    private DataGrid createSimpleDataGrid() throws DataGridException {
        DataGrid.Builder dataGridBuilder = new DataGrid.Builder();
        addColumnsTo(dataGridBuilder);
        addRowsTo(dataGridBuilder);
        return dataGridBuilder.build();
    }

    private DataGrid createSimpleDataGridWithMissingData() throws DataGridException {
        DataGrid.Builder dataGridBuilder = new DataGrid.Builder();
        addColumnsTo(dataGridBuilder);
        addRowsWithMissingDataTo(dataGridBuilder);
        return dataGridBuilder.build();
    }

    private DataGrid createSimpleDataGridWithoutData() throws DataGridException {
        DataGrid.Builder dataGridBuilder = new DataGrid.Builder();
        addColumnsTo(dataGridBuilder);
        return dataGridBuilder.build();
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
        dataGridBuilder.andData();
        DataGridRow row = new DataGridRow(ROW_ID_00);
        row.addReplaceValue(0, CELLVALUE_00_00);
        row.addReplaceValue(1, CELLVALUE_00_01);
        dataGridBuilder.withRow(row);
        row = new DataGridRow(ROW_ID_01);
        row.addReplaceValue(0, CELLVALUE_01_00);
        row.addReplaceValue(1, CELLVALUE_01_01);
        dataGridBuilder.withRow(row);
    }

    private void addRowsWithMissingDataTo(DataGrid.Builder dataGridBuilder) throws DataGridException {
        dataGridBuilder.andData();
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

    private String expectedResult() {
        return new StringBuilder()
                .append("<table>")
                .append(composeColsAndHead())
                .append("<tbody>")
                .append("<tr>")
                .append(composeIdCell(ROW_ID_00))
                .append("<td>").append(CELLVALUE_00_00).append("</td>")
                .append("<td>").append(CELLVALUE_00_01).append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append(composeIdCell(ROW_ID_01))
                .append("<td>").append(CELLVALUE_01_00).append("</td>")
                .append("<td>").append(CELLVALUE_01_01).append("</td>")
                .append("</tr>")
                .append("</tbody>")
                .append("</table>")
                .toString();
    }

    private String expectedResultWithMissingData() {
        return new StringBuilder()
                .append("<table>")
                .append(composeColsAndHead())
                .append("<tbody>")
                .append("<tr>")
                .append(composeIdCell(ROW_ID_00))
                .append("<td>").append(CELLVALUE_00_00).append("</td>")
                .append("<td>").append(CELLVALUE_00_01).append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append(composeIdCell(ROW_ID_01))
                .append("<td>").append(CELLVALUE_01_00).append("</td>")
                .append("<td>").append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append(composeIdCell(ROW_ID_02))
                .append("<td>").append(CELLVALUE_01_00).append("</td>")
                .append("<td>").append(CELLVALUE_01_01).append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append(composeIdCell(ROW_ID_03))
                .append("<td>").append("</td>")
                .append("<td>").append(CELLVALUE_01_01).append("</td>")
                .append("</tr>")
                .append("<tr>")
                .append(composeIdCell(ROW_ID_04))
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
                .append(composeColsAndHead())
                .append("<tbody>")
                .append("</tbody>")
                .append("</table>")
                .toString();
    }

    private String composeColsAndHead() {
        return new StringBuilder()
                .append("<colgroup>")
                .append("<col class=\"tv-col-id\" style=\"visibility:collapse\"></col>")
                .append("<col></col>")
                .append("<col></col>")
                .append("</colgroup>")
                .append("<thead>")
                .append("<tr>")
                .append("<th>").append("row-id").append("</th>")
                .append("<th>").append(COLHEADER_TITLE_00).append("</th>")
                .append("<th>").append(COLHEADER_TITLE_01).append("</th>")
                .append("</tr>")
                .append("</thead>")
                .toString();
    }

    private String composeIdCell(String id) {
        return new StringBuilder()
                .append("<td>").append(id == null ? "" : id).append("</td>")
                .toString();
    }

}
