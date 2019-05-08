/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

/**
 * A data source to provide table data for conversion.
 */
package org.tablevert.core.config;

public interface DataSource {

    /**
     * Gets the name of the data source.
     * @return the data source name
     */
    String getName();

    /**
     * Creates a clone of the data source.
     * @return the clone
     */
    DataSource clone();

}
