/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * Exception declaring the failure of a tablevert Builder class.
 */
public class BuilderFailedException extends TablevertCoreException {
    public BuilderFailedException(String message) {
        super(message);
    }
}
