/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.apache.poi.xssf.usermodel.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * {@link OutputGenerator} implementation for XLSX files.
 */
final class XlsxOutputGenerator implements OutputGenerator {

    private final class XlsxWorkInProgress {
        private final Integer CELLSTYLE_COLHEADER = 1;
        private final Integer CELLSTYLE_BODY = 2;

        private final XSSFWorkbook workbook;
        private final Map<Integer, XSSFCellStyle> cellStyleMap;
        private final XSSFSheet sheet;
        private int maxColIndex;

        private XlsxWorkInProgress() {
            this.workbook = new XSSFWorkbook();
            this.sheet = workbook.createSheet("Data");
            this.maxColIndex = -1;
            this.cellStyleMap = new Hashtable<>();
            initCellStyles();
        }

        private void initCellStyles() {
            // TODO: Intregrate proper cell styling including number and date formats for body cells
            XSSFCellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFont(workbook.createFont());
            headerCellStyle.getFont().setBold(true);
            cellStyleMap.put(CELLSTYLE_COLHEADER, headerCellStyle);

            XSSFCellStyle bodyCellStyle = workbook.createCellStyle();
            bodyCellStyle.setFont(workbook.createFont());
            bodyCellStyle.getFont().setBold(false);
            cellStyleMap.put(CELLSTYLE_BODY, bodyCellStyle);
        }

        private void addColumnHeaders(final List<DataGridColumn> columns) {
            XSSFRow row = sheet.createRow(0);
            for (DataGridColumn column : columns) {
                XSSFCell cell = row.createCell(column.getIndex());
                cell.setCellValue(column.getName());
                cell.setCellStyle(cellStyleMap.get(CELLSTYLE_COLHEADER));
                adjustMaxColIndex(column.getIndex());
            }
        }

        private void adjustMaxColIndex(int index) {
            if (index > maxColIndex) {
                maxColIndex = index;
            }
        }

        private void addValues(final DataGrid dataGrid) {
            for (int rowIndex = 0; rowIndex < dataGrid.getRowCount(); rowIndex++) {
                XSSFRow sheetRow = sheet.createRow(rowIndex + 1);
                DataGridRow dataGridRow = dataGrid.getRow(rowIndex);
                copyRowValues(dataGridRow, sheetRow);
            }
        }

        private void copyRowValues(final DataGridRow dataGridRow, final XSSFRow sheetRow) {
            if (dataGridRow == null) {
                return;
            }
            for (Integer colIndex : dataGridRow.getColumnIndicesOfValues()) {
                Object value = dataGridRow.getValue(colIndex);
                if (value != null) {
                    XSSFCell cell = sheetRow.createCell(colIndex);
                    // TODO: Add appropriate formatting
                    cell.setCellValue(value.toString());
                    cell.setCellStyle(cellStyleMap.get(CELLSTYLE_BODY));
                }
            }
        }

        private XlsxOutput packOutput() throws OutputGeneratorException {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try {
                workbook.write(outputStream);
            } catch (IOException e) {
                throw new OutputGeneratorException("Failed to write XLSX file to byte array");
            }
            return new XlsxOutput(outputStream.toByteArray());
        }

        private void finalizeStyling() {
            autoSizeColumns();
            freezeTopRow();
        }

        private void autoSizeColumns() {
            for (int i = 0; i <= maxColIndex; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        private void freezeTopRow() {
            sheet.createFreezePane(0, 1);
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
        XlsxWorkInProgress workInProgress = new XlsxWorkInProgress();
        workInProgress.addColumnHeaders(dataGrid.cloneColumns());
        workInProgress.addValues(dataGrid);
        workInProgress.finalizeStyling();
        return workInProgress.packOutput();
    }

}
