/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import java.io.OutputStream;

/**
 * Container for the output data provided by the {@link Tableverter}
 */
public interface Output {

    OutputFormat getFormat();

    void writeContent(OutputStream outputStream) throws OutputGeneratorException;

}
