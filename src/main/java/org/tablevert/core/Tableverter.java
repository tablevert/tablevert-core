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
     * Retrieves source table data, converts it to and returns it in the requested format.
     *
     * @param appliedQuery query with additional filtering and sorting
     * @param outputFormat format expected for the tableversion's output
     * @return the converted table
     */
    Output tablevert(AppliedQuery appliedQuery, OutputFormat outputFormat) throws Exception;



}
