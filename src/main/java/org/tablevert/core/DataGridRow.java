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

    private final String id;
    private final Map<Integer, Object> valueMap;

    DataGridRow(String id) {
        this.id = id;
        this.valueMap = new Hashtable<>();
    }

    void addReplaceValue(Integer colIndex, Object value) {
        if (colIndex == null || value == null) {
            return;
        }
        if (valueMap.containsKey(colIndex)) {
            valueMap.replace(colIndex, value);
        } else {
            valueMap.put(colIndex, value);
        }
    }

    String getId() {
        return id;
    }

    List<Integer> getColumnIndicesOfValues() {
        return new ArrayList<>(valueMap.keySet());
    }

    Object getValue(int colIndex) {
        if (!valueMap.containsKey(colIndex)) {
            return null;
        }
        // TODO: Ensure read-only on the value
        return valueMap.get(colIndex);
    }

}
