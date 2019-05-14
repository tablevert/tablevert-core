/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class XlsxOutputGeneratorTest {

    private static final String CLASSNAME_STRING = "java.lang.String";

    private static final String COLHEADER_TITLE_00 = "A";
    private static final String COLHEADER_TITLE_01 = "B";

    private static final String ROW_ID_00 = "0";
    private static final String ROW_ID_01 = "1";

    private static final String CELLVALUE_00_00 = "value (0, 0)";
    private static final String CELLVALUE_00_01 = "value (0, 1)";
    private static final String CELLVALUE_01_00 = "value (1, 0)";
    private static final String CELLVALUE_01_01 = "value (1, 1)";

    @Test
    public void createsXLSXOutput() throws Exception {
        DataGrid dataGrid = createSimpleDataGrid();
        OutputGenerator outputGenerator = new XlsxOutputGenerator();

        Output output = outputGenerator.process(dataGrid);

        // TODO: Replace this with an automated output check!!!
        writeOutputToFile(output);

        assertNotNull(output);
    }

    private DataGrid createSimpleDataGrid() throws DataGridException {
        DataGrid dataGrid = new DataGrid();
        dataGrid.addColumn(
                new DataGridColumn(0, COLHEADER_TITLE_00, CLASSNAME_STRING)
        );
        dataGrid.addColumn(
                new DataGridColumn(1, COLHEADER_TITLE_01, CLASSNAME_STRING)
        );
        DataGridRow row = new DataGridRow(ROW_ID_00);
        row.addReplaceValue(0, CELLVALUE_00_00);
        row.addReplaceValue(1, CELLVALUE_00_01);
        dataGrid.addRow(row);
        row = new DataGridRow(ROW_ID_01);
        row.addReplaceValue(0, CELLVALUE_01_00);
        row.addReplaceValue(1, CELLVALUE_01_01);
        dataGrid.addRow(row);
        return dataGrid;
    }

    private void writeOutputToFile(Output output) throws Exception {
        if (output == null) {
            return;
        }
        OutputStream outputStream = new FileOutputStream("target/test_output.xlsx");
        output.writeContent(outputStream);
    }


}
