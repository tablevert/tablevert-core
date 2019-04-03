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
 * Implementation of the {@link org.tablevert.core.Tableverter} interface for database sources.
 */
public final class DatabaseTableverter implements Tableverter {

    private final Logger logger = LoggerFactory.getLogger(DatabaseTableverter.class);

    private TablevertConfig tablevertConfig;

    public DatabaseTableverter(TablevertConfig config) {
        this.tablevertConfig = config;
        logger.info("Set up DatabaseTableverter");
    }

    /**
     * Retrieves source table data from a database and converts it to the requested format.
     *
     * @param appliedQuery database query with additional filtering and sorting
     * @param outputFormat format expected for the tableversion's output
     * @return the converted table
     */
    @Override
    public Output tablevert(AppliedQuery appliedQuery, OutputFormat outputFormat) throws TablevertCoreException {
        DataGrid dataGrid = retrieveDataFor(appliedQuery);
        return generateOutput(dataGrid, outputFormat);
    }

    private DataGrid retrieveDataFor(AppliedQuery appliedQuery) throws TablevertCoreException {
        DatabaseReader databaseReader = prepareReaderFor(appliedQuery);
        return databaseReader.read(composeEffectiveQueryStatement(appliedQuery));
    }

    private Output generateOutput(DataGrid dataGrid, OutputFormat outputFormat) throws TablevertCoreException {
        OutputGenerator outputGenerator = selectGeneratorFor(outputFormat);
        return outputGenerator.process(dataGrid);
    }

    private DatabaseReader prepareReaderFor(AppliedQuery appliedQuery) throws BuilderFailedException {
        Database database = tablevertConfig.getDatabaseForQuery(appliedQuery.getBaseQueryName());
        DatabaseReader.Builder readerBuilder = selectReaderBuilderFor(database.getDbType());
        return readerBuilder
                .forDatabase(database)
                .withUser(appliedQuery.getUserName())
                .build();
    }

    private DatabaseReader.Builder selectReaderBuilderFor(DatabaseType dbType) throws BuilderFailedException {
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

    private String composeEffectiveQueryStatement(AppliedQuery appliedQuery) {
        // TODO: Integrate applied filtering and sorting
        DatabaseQuery databaseQuery = tablevertConfig.getDatabaseQuery(appliedQuery.getBaseQueryName());
        return databaseQuery.getQueryStatement();
    }

}
