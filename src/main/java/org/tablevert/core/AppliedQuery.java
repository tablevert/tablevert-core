/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * A query with context-related filtering and sorting.
 */
public interface AppliedQuery {

    /**
     * Gets the name of the underlying configured base query.
     *
     * @return the base query name
     */
    String getBaseQueryName();

}
