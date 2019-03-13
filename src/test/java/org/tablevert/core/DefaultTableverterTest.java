/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DefaultTableverterTest {

    private final DefaultTableverter defaultTableverter = new DefaultTableverter();

    @Test
    public void returnsDummyDummyString() {
        String dummy = defaultTableverter.getDummyDummyString();
        Assertions.assertTrue(dummy.contains("dummy string"));
    }

}
