/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * Abstract parent class for exceptions thrown by Tableverter methods.
 */
public abstract class TablevertCoreException extends Exception {
    TablevertCoreException(String message) {
        super(message);
    }

    TablevertCoreException(Exception e) {
        super(e);
    }

    TablevertCoreException(String message, Exception e) {
        super(message, e);
    }
}

