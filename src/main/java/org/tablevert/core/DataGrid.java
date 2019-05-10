/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Representation of table data within the tablevert core.
 */
final class DataGrid {

    private Map<Integer, DataGridColumn> columns;
    private List<DataGridRow> rows;

    DataGrid() {
        this.columns = new Hashtable<>();
        this.rows = new ArrayList<>();
    }

    void addColumn(DataGridColumn column) {
        columns.putIfAbsent(column.getIndex(), column);
    }

    void addRow(DataGridRow row) {
        rows.add(row);
    }

    List<DataGridColumn> definedColumns() {
        return columns.values().stream().map(DataGridColumn::clone).collect(Collectors.toList());
    }

    List<DataGridColumn> allColumns() {
        List<DataGridColumn> allCols = new ArrayList<>();
        if (columns.isEmpty()) {
            return allCols;
        }
        int maxColIndex = columns.keySet().stream().mapToInt(index -> index).max().getAsInt();
        for (int i = 0; i <= maxColIndex; i++) {
            allCols.add(columns.containsKey(i) ? columns.get(i).clone() : null);
        }
        return allCols;
    }

    DataGridRow getRow(int rowIndex) {
        return rowIndex >= rows.size() ? null : rows.get(rowIndex);
    }

    int getDefinedColumnCount() {
        return columns.size();
    }

    int getRowCount() {
        return rows.size();
    }




}
