/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A single row within a {@link DataGrid} object.
 */
final class DataGridRow {

    private final int index;
    private final Map<Integer, Object> valueMap;
    private boolean valuesLocked;

    DataGridRow(int index) {
        this.index = index;
        this.valueMap = new Hashtable<>();
        this.valuesLocked = false;
    }

    void addReplaceValue(Integer colIndex, Object value) {
        if (valuesLocked || colIndex == null || value == null) {
            return;
        }
        if (valueMap.containsKey(colIndex)) {
            valueMap.replace(colIndex, value);
        } else {
            valueMap.put(colIndex, value);
        }
    }

    int getIndex() {
        return index;
    }

    List<Integer> getColumnIndicesOfValues() {
        return new ArrayList<>(valueMap.keySet());
    }

    Object getValue(int colIndex) {
        lockValues();
        if (!valueMap.containsKey(colIndex)) {
            return null;
        }
        // TODO: Ensure read-only on the value
        return valueMap.get(colIndex);
    }

    void lockValues() {
        valuesLocked = true;
    }
}
