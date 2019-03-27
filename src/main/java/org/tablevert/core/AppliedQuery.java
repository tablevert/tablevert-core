/*
 * Copyright 2019 doc-hil
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

    /**
     * @return the name of the user to apply to this query
     */
    String getUserName();



}
