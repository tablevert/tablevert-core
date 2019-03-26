/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * Entry point for providing the tablevert conversion functionality.
 *
 * @author doc-hil
 */
public interface Tableverter {

    /**
     * Retrieves source table data from a database and converts it to the requested format.
     *
     * @param appliedQuery database query with additional filtering and sorting
     * @param outputFormat format expected for the tableversion's output
     * @return the converted table
     */
    Output tablevertFromDatabase(AppliedDatabaseQuery appliedQuery, OutputFormat outputFormat) throws Exception;



}
