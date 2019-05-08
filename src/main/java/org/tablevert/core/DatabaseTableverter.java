/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tablevert.core.config.*;

/**
 * Implementation of the {@link org.tablevert.core.Tableverter} interface for database sources.
 */
public final class DatabaseTableverter implements Tableverter {

    private final Logger logger = LoggerFactory.getLogger(DatabaseTableverter.class);

    private TablevertConfig tablevertConfig;

    DatabaseTableverter(TablevertConfig config) {
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
        return databaseReader.read();
    }

    private Output generateOutput(DataGrid dataGrid, OutputFormat outputFormat) throws TablevertCoreException {
        OutputGenerator outputGenerator = selectGeneratorFor(outputFormat);
        return outputGenerator.process(dataGrid);
    }

    private DatabaseReader prepareReaderFor(AppliedQuery appliedQuery) throws BuilderFailedException {
        DataSource database = tablevertConfig.getDataSourceForQuery(appliedQuery.getBaseQueryName());
        if (!Database.class.equals(database.getClass())) {
            throw new BuilderFailedException("Data source is not a Database object; actual class name is ["
                    + database.getClass().getSimpleName() + "]");
        }
        DatabaseReader.Builder readerBuilder = selectReaderBuilderFor(((Database)database).getDbType());
        return readerBuilder
                .usingConfig(tablevertConfig)
                .forAppliedQuery(appliedQuery)
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

}
