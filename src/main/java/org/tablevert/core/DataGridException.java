/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * Exception thrown by a {@link DataGrid} handling class.
 */
public class DataGridException extends TablevertCoreException {
    DataGridException(String message) {
        super(message);
    }

    DataGridException(String message, Exception e) {
        super(message, e);
    }
}
