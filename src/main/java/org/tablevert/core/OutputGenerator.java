/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * Generator for creating an output table from a {@link DataGrid} object.
 */
interface OutputGenerator {

    /**
     * Runs the generator.
     *
     * @param dataGrid the data to handle
     * @return the generated output
     */
    Output process(DataGrid dataGrid) throws OutputGeneratorException;

}
