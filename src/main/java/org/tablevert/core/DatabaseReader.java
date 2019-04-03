/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.tablevert.core.config.Database;

/**
 * Source data provider for databases.
 */
interface DatabaseReader {

    interface Builder {
        Builder forDatabase(Database database);
        Builder withUser(String userName);
        DatabaseReader build() throws BuilderFailedException;
    }

    DataGrid read(String queryStatement) throws TablevertCoreException;

}
