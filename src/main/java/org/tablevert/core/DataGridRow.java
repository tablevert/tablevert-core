/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import java.util.Hashtable;
import java.util.Map;

/**
 * A single row within a {@link DataGrid} object.
 */
class DataGridRow {
    private Map<Integer, Object> valueMap;

    DataGridRow() {
        valueMap = new Hashtable<>();
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
}
