/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

/**
 * Tablevert configuration to be passed to the Tableverter instance.
  */
public interface TablevertConfig {

    PredefinedQuery getPredefinedQuery(String queryName);

    DataSource getDataSourceForQuery(String queryName);

    TablevertConfig clone();

}
