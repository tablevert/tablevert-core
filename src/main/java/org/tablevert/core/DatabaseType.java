/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

enum DatabaseType {
    POSTGRESQL("postgresql");

    private String name;

    DatabaseType(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }
}
