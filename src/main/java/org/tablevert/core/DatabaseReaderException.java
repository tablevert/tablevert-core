/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * Exception called when failing to execute {@link DatabaseReader} methods.
 */
public class DatabaseReaderException extends TablevertCoreException {
    DatabaseReaderException(String message) {
        super(message);
    }

    DatabaseReaderException(Exception e) {
        super(e);
    }
}
