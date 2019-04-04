/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.tablevert.core.config.TablevertConfig;

/**
 * Factory for objects implementing the {@link Tableverter} interface.
 */
public class TableverterFactory {

    /**
     * Creates a {@link DatabaseTableverter} object for the specified configuration.
     *
     * @param config the configuration to apply
     * @return the Tableverter instance
     */
    public Tableverter createDatabaseTableverterFor(TablevertConfig config) {
        return new DatabaseTableverter(config);
    }

}
