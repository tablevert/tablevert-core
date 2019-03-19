/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.util.List;

/**
 * {@link OutputGenerator} implementation for XLSX files.
 */
final class XLSXOutputGenerator implements OutputGenerator {

    /**
     * Processes the output generation.
     *
     * @param dataGrid the data to handle
     * @return the generated output
     */
    @Override
    public Output process(final DataGrid dataGrid) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Data");

        XSSFCellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.getFont().setBold(true);
        addColumnHeadersToSheet(dataGrid.cloneColumns(), sheet, headerCellStyle);

        addValuesToSheet(dataGrid, sheet);

        return packOutput(workbook);
    }

//    private XSSFWorkbook createWorkbook() {
//        return new XSSFWorkbook();
//    }

    private void addColumnHeadersToSheet(final List<DataGridColumn> columns, final XSSFSheet sheet, final XSSFCellStyle cellStyle) {
        XSSFRow row = sheet.createRow(0);
        for (DataGridColumn column : columns) {
            XSSFCell cell = row.createCell(column.getIndex());
            cell.setCellValue(column.getName());
            cell.setCellStyle(cellStyle);
        }
    }

    private void addValuesToSheet(final DataGrid dataGrid, final XSSFSheet sheet) {
        for (int rowIndex = 0; rowIndex < dataGrid.getRowCount(); rowIndex++) {
            XSSFRow sheetRow = sheet.createRow(rowIndex);
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
            }
        }

    }

    private XLSXOutput packOutput(XSSFWorkbook workbook) {
        XLSXOutput output = new XLSXOutput();
        // TODO: Add functionality!!!

        return output;
    }

}
