/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.tablevert.core.config.TablevertConfig;

/**
 * Source data provider for databases.
 */
interface DatabaseReader {

    interface Builder {
        Builder usingConfig(TablevertConfig tablevertConfig);
        Builder forAppliedQuery(AppliedQuery appliedQuery);
        DatabaseReader build() throws BuilderFailedException;
    }

    DataGrid read() throws TablevertCoreException;

}
