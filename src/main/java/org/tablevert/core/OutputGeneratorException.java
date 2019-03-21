/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * Exception thrown by a subclass of {@link OutputGenerator}.
 */
public class OutputGeneratorException extends Exception {
    OutputGeneratorException(String message) {
        super(message);
    }
}
