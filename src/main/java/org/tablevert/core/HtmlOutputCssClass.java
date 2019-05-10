/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

public enum HtmlOutputCssClass {
    TV_COL_DEFAULT,

    TV_NONE;

    @Override
    public String toString() {
        return this.name().replace('_', '-').toLowerCase();
    }

}
