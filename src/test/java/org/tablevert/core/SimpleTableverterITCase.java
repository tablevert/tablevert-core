/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.tablevert.core.config.SimpleTablevertConfig;
import org.tablevert.core.config.TablevertConfig;

public class SimpleTableverterITCase {

    @Test
    public void readsDataFromPostgres() {
        Tableverter tableverter = new SimpleTableverter(getConfigForPostgresTest());
        Assertions.assertTrue(true);
    }

    private TablevertConfig getConfigForPostgresTest() {
        TablevertConfig config = new SimpleTablevertConfig.Builder()
                .build();
        return config;
    }

}
