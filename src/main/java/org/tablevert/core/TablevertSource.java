/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * Data source of a tablevert operation.
 */
interface TablevertSource {
    TablevertSourceType getSourceType();

    DataGrid fetchData(Object fetchInstruction) throws Exception;
}
