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

    static final class Builder {

        private Map<Integer, DataGridColumn> columns;
        private List<DataGridRow> rows;
        private boolean dataSectionIsBegun;

        Builder() {
            this.columns = new Hashtable<>();
            this.rows = new ArrayList<>();
            dataSectionIsBegun = false;
        }

        Builder withColumn(DataGridColumn column) throws DataGridException {
            ensureDataSectionIsNotBegun();
            columns.putIfAbsent(column.getIndex(), column);
            return this;
        }

        Builder andData() throws DataGridException {
            if (columns.isEmpty()) {
                throw new DataGridException("Data section must not be started with without columns defined.");
            }
            dataSectionIsBegun = true;
            return this;
        }

        Builder withRow(DataGridRow row) throws DataGridException {
            ensureDataSectionIsBegun();
            ensureUniqueRowId(row.getId(), rows.size());
            rows.add(row);
            return this;
        }

        DataGrid build() throws DataGridException {
            validate();
            return new DataGrid(columns, rows);
        }

        List<DataGridColumn> definedColumns() {
            return columns.values().stream().map(DataGridColumn::clone).collect(Collectors.toList());
        }

        private void ensureDataSectionIsBegun() throws DataGridException {
            if (!dataSectionIsBegun) {
                throw new DataGridException("Cannot add rows before the data section is begun.");
            }
        }

        private void ensureDataSectionIsNotBegun() throws DataGridException {
            if (dataSectionIsBegun) {
                throw new DataGridException("Cannot add columns after the data section was begun.");
            }
        }

        private void ensureUniqueRowId(String id, long index) throws DataGridException {
            if (id == null || id.isEmpty()) {
                throw new DataGridException("Row id must not be empty for row no. " + index);
            }
            if (rows.stream().filter(row -> id.equals(row.getId())).count() > 0) {
                throw new DataGridException(String.format("Duplicate Id [%s] assigned to row no. %d", id, index));
            }
        }

        private void validate() throws DataGridException {
            String errors = "";
            if (columns.isEmpty()) {
                errors += " - no columns have been defined;";
            }
            if (!errors.isEmpty()) {
                throw new DataGridException("DataGrid validation failed with errors: " + errors);
            }

        }

    }

    private Map<Integer, DataGridColumn> columns;
    private List<DataGridRow> rows;

    private DataGrid(Map<Integer, DataGridColumn> columns, List<DataGridRow> rows) {
        this.columns = columns;
        this.rows = rows;
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
        return (rowIndex < 0 || rowIndex >= rows.size()) ? null : rows.get(rowIndex);
    }

    int getDefinedColumnCount() {
        return columns.size();
    }

    long getRowCount() {
        return rows.size();
    }

}
