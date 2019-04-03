/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

/**
 * Tablevert configuration to be passed to the Tableverter instance.
 *
 * @author doc-hil
 */
public interface TablevertConfig {

    DatabaseQuery getDatabaseQuery(String queryName);

    Database getDatabaseForQuery(String queryName);

    TablevertConfig clone();

}
