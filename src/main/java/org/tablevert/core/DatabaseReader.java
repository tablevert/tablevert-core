/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * Source data provider for databases.
 *
 * @author doc-hil
 */
interface DatabaseReader {

    DataGrid fetchData(DatabaseQuery databaseQuery) throws Exception;

}
