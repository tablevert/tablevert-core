/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * Exception declaring the failure of a tablevert Builder class.
 */
public class BuilderFailedException extends Exception {
    public BuilderFailedException(String message) {
        super(message);
    }
}
