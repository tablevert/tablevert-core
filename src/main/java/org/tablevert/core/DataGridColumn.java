/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * A single column within a {@link DataGrid} instance.
 */
class DataGridColumn {
    private String name;
    private String className;

    DataGridColumn(String name, String className) {
        this.name = name;
        this.className = className;
    }

    String getName() {
        return name;
    }

    String getClassName() {
        return className;
    }
}
