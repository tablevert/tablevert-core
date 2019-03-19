/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * A single column within a {@link DataGrid} instance.
 */
final class DataGridColumn implements Cloneable {
    private final int index;
    private final String name;
    private final String javaClassName;

    DataGridColumn(int index, String name, String javaClassName) {
        this.index = index;
        this.name = name;
        this.javaClassName = javaClassName;
    }

    int getIndex() {
        return index;
    }

    String getName() {
        return name;
    }

    String getJavaClassName() {
        return javaClassName;
    }

    @Override
    protected DataGridColumn clone() {
        return new DataGridColumn(index, name, javaClassName);
    }

}
