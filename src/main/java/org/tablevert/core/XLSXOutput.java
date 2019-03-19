/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

final class XLSXOutput implements Output {

    @Override
    public OutputFormat getFormat() {
        return OutputFormat.XLSX;
    }

}
