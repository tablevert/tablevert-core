/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.tablevert.core.config.Database;
import org.tablevert.core.config.DatabaseQuery;

/**
 * Source data provider for databases.
 *
 * @author doc-hil
 */
interface DatabaseReader {

    interface Builder {
        Builder forDatabase(Database database);
        Builder withUser(String userName);
        DatabaseReader build() throws BuilderFailedException;
    }

    DataGrid fetchData(String queryStatement) throws Exception;

}
