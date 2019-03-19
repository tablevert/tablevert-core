/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tablevert.core.config.TablevertConfig;

/**
 * Simple implementation of the {@link org.tablevert.core.Tableverter} interface.
 *
 * @author doc-hil
 */
public final class SimpleTableverter implements Tableverter {

    final Logger logger = LoggerFactory.getLogger(SimpleTableverter.class);

    TablevertConfig tablevertConfig;

    public SimpleTableverter(TablevertConfig config) {
        this.tablevertConfig = config;
        logger.info("Set up SimpleTableverter");

    }

    /**
     * Provides the tablevert output for the specified query in the specified format.
     *
     * @param appliedQuery query with additional filtering and sorting
     * @param outputFormat format expected for the tableversion's output
     * @return the converted table
     */
    @Override
    public Output tablevert(AppliedQuery appliedQuery, OutputFormat outputFormat) throws Exception {

        DataGrid dataGrid = retrieveDataFor(appliedQuery);

        OutputGenerator outputGenerator = selectGeneratorFor(outputFormat);
        return outputGenerator.process(dataGrid);
    }

    private DataGrid retrieveDataFor(AppliedQuery appliedQuery) throws Exception {
        // TODO: Include non-database readers
        DatabaseReader databaseReader = selectDatabaseReaderFor(appliedQuery);
        return databaseReader.fetchData(null);
    }

    private DatabaseReader selectDatabaseReaderFor(AppliedQuery appliedQuery) throws BuilderFailedException {
        // TODO: Include other readers and adapt to appliedQuery
        return new JdbcDatabaseReader.Builder(DatabaseType.POSTGRESQL)
                .forHost("localhost")
                .forDatabase("dummy")
                .withCredentials("x", "x")
                .build();
    }

    private OutputGenerator selectGeneratorFor(OutputFormat outputFormat) {
        switch (outputFormat) {
            case XLSX:
                return new XLSXOutputGenerator();
            default:
                throw new IllegalArgumentException("No OutputGenerator defined for OutputFormat " + outputFormat);
        }
    }

}
