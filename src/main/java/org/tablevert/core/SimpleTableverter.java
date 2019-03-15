/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tablevert.core.config.TablevertConfig;

/**
 * Simple implementation of the {@link org.tablevert.core.Tableverter} interface.
 *
 * @author doc-hil
 */
public class SimpleTableverter implements Tableverter {

    final Logger logger = LoggerFactory.getLogger(SimpleTableverter.class);

    TablevertConfig tablevertConfig;

    public SimpleTableverter(TablevertConfig config) {
        this.tablevertConfig = config;
        logger.info("Set up SimpleTableverter");

    }

    public String getDummyDummyString() {
        return "Ain't this a dummy string?";
    }
}
