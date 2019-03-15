/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tablevert.core.config.SimpleTablevertConfig;
import org.tablevert.core.config.TablevertConfig;

public class SimpleTableverterTest {

    private Tableverter tableverter;

    @BeforeEach
    public void setUp() {
        TablevertConfig config = new SimpleTablevertConfig.Builder().build();
        tableverter = new SimpleTableverter(config);
    }

    @Test
    public void returnsDummyDummyString() {
        String dummy = tableverter.getDummyDummyString();
        Assertions.assertTrue(dummy.contains("dummy string"));
    }

}
