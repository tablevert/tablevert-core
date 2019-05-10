/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.tablevert.core.HtmlOutput.Column;
import org.tablevert.core.HtmlOutput.DataRow;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link OutputGenerator} implementation for HTML files.
 */
final class HtmlOutputGenerator implements OutputGenerator {

    private final class HtmlWorkInProgress {
        private final List<Column> columns;
        private final List<DataRow> rows;
        private int maxColIndex;

        private HtmlWorkInProgress() {
            this.columns = new ArrayList<>();
            this.rows = new ArrayList<>();
            this.maxColIndex = -1;
        }

        private HtmlWorkInProgress forDataGrid(final DataGrid dataGrid) {
            convertColumns(dataGrid.allColumns());
            convertRows(dataGrid);
            return this;
        }

        private void convertColumns(final List<DataGridColumn> columns) {
            for (DataGridColumn column : columns) {
                Column htmlCol = new Column(String.valueOf(column.getIndex()), column.getName());
                this.columns.add(htmlCol);
                adjustMaxColIndex(column.getIndex());
            }
        }

        private void adjustMaxColIndex(int index) {
            if (index > maxColIndex) {
                maxColIndex = index;
            }
        }

        private void convertRows(final DataGrid dataGrid) {
            for (int rowIndex = 0; rowIndex < dataGrid.getRowCount(); rowIndex++) {
                DataGridRow dataGridRow = dataGrid.getRow(rowIndex);
                rows.add(convertSingleRow(dataGridRow));
            }
        }

        private DataRow convertSingleRow(final DataGridRow gridRow) {
            if (gridRow == null) {
                return null;
            }
            DataRow dataRow = new DataRow(String.valueOf(gridRow.getIndex()));
            for (int colIndex = 0; colIndex <= maxColIndex; colIndex++) {
                Object value = gridRow.getValue(colIndex);
                // TODO: Add appropriate formatting
                dataRow.addValue(value == null ? null : value.toString());
            }
            return dataRow;
        }

        private HtmlOutput packOutput() throws OutputGeneratorException {
            try {
                return new HtmlOutput.Builder()
                        .withColumns(columns)
                        .withRows(rows)
                        .fullTable();
            } catch (Exception e) {
                throw new OutputGeneratorException("Failed to write HTML table to string", e);
            }
        }

    }

    /**
     * Processes the output generation.
     *
     * @param dataGrid the data to handle
     * @return the generated output
     */
    @Override
    public Output process(final DataGrid dataGrid) throws OutputGeneratorException {
        return new HtmlWorkInProgress()
                .forDataGrid(dataGrid)
                .packOutput();
    }

}
