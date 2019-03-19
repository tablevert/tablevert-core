/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * A query with additional filtering and sorting, as required by the context where it is applied.
 */
public interface AppliedQuery {

    String getBaseQueryName();

}
