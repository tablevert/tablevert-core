/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

public enum DatabaseType {
    POSTGRESQL("postgresql");

    private String name;

    DatabaseType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getSelectStatementTemplate() {
        switch (this) {
            default:
                return "SELECT %s FROM %s %s %s;";
        }
    }
}
