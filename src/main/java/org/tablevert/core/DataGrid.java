/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Representation of table data within the tablevert core.
 */
final class DataGrid {

    private List<DataGridColumn> columns;
    private List<DataGridRow> rows;

    DataGrid() {
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    void addColumn(DataGridColumn column) {
        columns.add(column);
    }

    void addRow(DataGridRow row) {
        rows.add(row);
    }

    List<DataGridColumn> cloneColumns() {
        return columns.stream().map(column -> column.clone()).collect(Collectors.toList());
    }

    DataGridRow getRow(int rowIndex) {
        return rowIndex >= rows.size() ? null : rows.get(rowIndex);
    }

    int getColumnCount() {
        return columns.size();
    }

    int getRowCount() {
        return rows.size();
    }




}
