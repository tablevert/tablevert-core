/*
Copyright 2019 doc-hil
SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of the {@link org.tablevert.core.Tableverter} interface.
 *
 * @author doc-hil
 */
public class DefaultTableverter implements Tableverter {
    final Logger logger = LoggerFactory.getLogger(DefaultTableverter.class);

    public DefaultTableverter() {
        logger.info("Set up DefaultTableverter");

    }

    public String getDummyDummyString() {
        return "Ain't this a dummy string?";
    }
}
