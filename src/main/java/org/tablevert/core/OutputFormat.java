/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * Available output formats of the {@link Tableverter}.
 */
public enum OutputFormat {
    HTML, XLSX;

    public boolean isDownloadable() {
        switch (this) {
            case XLSX:
                return true;
            default:
                return false;
        }
    }

}
