/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * Exception called when failing to execute {@link DatabaseReader} methods.
 */
class DatabaseReaderException extends Exception {
    public DatabaseReaderException(String message) {
        super(message);
    }
}
