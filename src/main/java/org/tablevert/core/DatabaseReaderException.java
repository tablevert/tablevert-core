/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * Exception called when failing to execute {@link DatabaseReader} methods.
 */
public class DatabaseReaderException extends Exception {
    DatabaseReaderException(String message) {
        super(message);
    }
}
