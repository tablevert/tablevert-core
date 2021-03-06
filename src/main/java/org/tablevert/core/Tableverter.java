/*
 * Copyright 2019 conis Informationssysteme GmbH
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
     * Retrieves source table data and converts it to the requested format.
     *
     * @param appliedQuery query with context-related filtering and sorting
     * @param outputFormat format expected for the tableversion's output
     * @return the converted table
     * @throws TablevertCoreException exception related to Tablevert core functionality
     */
    Output tablevert(AppliedQuery appliedQuery, OutputFormat outputFormat) throws TablevertCoreException;

}
