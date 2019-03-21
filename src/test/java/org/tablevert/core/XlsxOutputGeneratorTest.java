/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Test;

import java.io.FileOutputStream;
import java.io.OutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class XlsxOutputGeneratorTest {

    private static final String CLASSNAME_STRING = "java.lang.String";

    @Test
    public void createsXLSXOutput() throws Exception {
        DataGrid dataGrid = createSimpleDataGrid();
        OutputGenerator outputGenerator = new XlsxOutputGenerator();

        Output output = outputGenerator.process(dataGrid);

        // TODO: Replace this with an automated output check!!!
        writeOutputToFile(output);

        assertNotNull(output);
    }

    private DataGrid createSimpleDataGrid() {
        DataGrid dataGrid = new DataGrid("test");
        dataGrid.addColumn(
                new DataGridColumn(0, "A", CLASSNAME_STRING)
        );
        dataGrid.addColumn(
                new DataGridColumn(1, "B", CLASSNAME_STRING)
        );
        DataGridRow row = new DataGridRow(0);
        row.addReplaceValue(0, "FirstValue");
        row.addReplaceValue(1, "SecondValue");
        dataGrid.addRow(row);
        row = new DataGridRow(1);
        row.addReplaceValue(0, "ThirdValue");
        row.addReplaceValue(1, "FourthValue");
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
