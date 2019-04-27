/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import java.io.IOException;
import java.io.OutputStream;

public final class XlsxOutput implements Output {

    private byte[] content;

    XlsxOutput(byte[] content) {
        this.content = content;
    }

    @Override
    public OutputFormat getFormat() {
        return OutputFormat.XLSX;
    }

    @Override
    public void writeContent(OutputStream outputStream) throws OutputGeneratorException {
        try {
            outputStream.write(content);
        } catch (IOException e) {
            throw new OutputGeneratorException("Failed to write content to output stream");
        }
    }

}
