/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class XLSXOutputGeneratorTest {

    private static final String CLASSNAME_STRING = "java.lang.String";

    @Test
    public void createsXLSXOutput() {
        DataGrid dataGrid = createSimpleDataGrid();
        OutputGenerator outputGenerator = new XLSXOutputGenerator();

        Output output = outputGenerator.process(dataGrid);

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


}
