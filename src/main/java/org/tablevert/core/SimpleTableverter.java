/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tablevert.core.config.Database;
import org.tablevert.core.config.DatabaseQuery;
import org.tablevert.core.config.DatabaseType;
import org.tablevert.core.config.TablevertConfig;

/**
 * Simple implementation of the {@link org.tablevert.core.Tableverter} interface.
 *
 * @author doc-hil
 */
public final class SimpleTableverter implements Tableverter {

    private final Logger logger = LoggerFactory.getLogger(SimpleTableverter.class);

    private TablevertConfig tablevertConfig;

    public SimpleTableverter(TablevertConfig config) {
        this.tablevertConfig = config;
        logger.info("Set up SimpleTableverter");
    }

    /**
     * Retrieves source table data from a database and converts it to the requested format.
     *
     * @param appliedQuery database query with additional filtering and sorting
     * @param outputFormat format expected for the tableversion's output
     * @return the converted table
     */
    @Override
    public Output tablevertFromDatabase(AppliedDatabaseQuery appliedQuery, OutputFormat outputFormat) throws Exception {
        DataGrid dataGrid = retrieveDatabaseDataFor(appliedQuery);
        OutputGenerator outputGenerator = selectGeneratorFor(outputFormat);
        return outputGenerator.process(dataGrid);
    }

    private DataGrid retrieveDatabaseDataFor(AppliedDatabaseQuery appliedQuery) throws Exception {
        DatabaseReader databaseReader = prepareDatabaseReaderFor(appliedQuery);
        return databaseReader.fetchData(composeEffectiveQueryStatement(appliedQuery));
    }

    private DatabaseReader prepareDatabaseReaderFor(AppliedDatabaseQuery appliedQuery) throws BuilderFailedException {
        Database database = tablevertConfig.getDatabaseForQuery(appliedQuery.getBaseQueryName());
        DatabaseReader.Builder readerBuilder = selectReaderBuilderForDatabaseType(database.getDbType());
        return readerBuilder
                .forDatabase(database)
                .withUser(appliedQuery.getUserName())
                .build();
    }

    private DatabaseReader.Builder selectReaderBuilderForDatabaseType(DatabaseType dbType) throws BuilderFailedException {
        switch (dbType) {
            case POSTGRESQL:
                return new JdbcDatabaseReader.Builder();
            default:
                throw new BuilderFailedException("No DatabaseReader.Builder implementation found for database type ["
                + dbType.name() + "]");
        }
    }

    private OutputGenerator selectGeneratorFor(OutputFormat outputFormat) {
        switch (outputFormat) {
            case XLSX:
                return new XlsxOutputGenerator();
            default:
                throw new IllegalArgumentException("No OutputGenerator defined for OutputFormat " + outputFormat);
        }
    }

    private String composeEffectiveQueryStatement(AppliedDatabaseQuery appliedQuery) {
        // TODO: Integrate applied filtering and sorting
        DatabaseQuery databaseQuery = tablevertConfig.getDatabaseQuery(appliedQuery.getBaseQueryName());
        return databaseQuery.getQueryStatement();
    }

}
