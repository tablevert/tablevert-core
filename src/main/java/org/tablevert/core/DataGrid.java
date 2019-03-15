/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Representation of table data within the tablevert core.
 */
class DataGrid {

    private String queryName;
    private List<DataGridColumn> columns;
    private List<DataGridRow> rows;

    DataGrid(String queryName) {
        this.queryName = queryName;
        this.columns = new ArrayList<>();
        this.rows = new ArrayList<>();
    }

    void addColumn(DataGridColumn column) {
        columns.add(column);
    }

    void addRow(DataGridRow row) {
        rows.add(row);
    }

}
