/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

/**
 * A pre-configured query to retrieve data from a {@link DataSource}.
 */
public interface PredefinedQuery {
    /**
     * Gets the name of the query.
     *
     * @return the query name
     */
    String getName();

    /**
     * @return the name of the data source to access
     */
    String getDataSourceName();

    /**
     * Creates a clone of the query.
     * @return the clone
     */
    PredefinedQuery clone();
}
