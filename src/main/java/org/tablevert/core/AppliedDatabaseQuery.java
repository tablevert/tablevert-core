/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * A database query, where the underlying {@link org.tablevert.core.config.DatabaseQuery} instance is extended with
 * context-specific filtering and sorting.
 */
public class AppliedDatabaseQuery implements AppliedQuery {

    /**
     * Builder for {@link AppliedDatabaseQuery} instances.
     */
    public static class Builder {
        private String baseQueryName;

        /**
         * Sets the {@link org.tablevert.core.config.DatabaseQuery} reference.
         *
         * @param baseQueryName the name of the underlying base query
         * @return the builder
         */
        public Builder forDatabaseQuery(String baseQueryName) {
            this.baseQueryName = baseQueryName;
            return this;
        }

        /**
         * Builds the {@link AppliedDatabaseQuery} object.
         *
         * @return the built object
         */
        public AppliedDatabaseQuery build() {
            // TODO: Add validation
            AppliedDatabaseQuery appliedQuery = new AppliedDatabaseQuery();
            appliedQuery.baseQueryName = this.baseQueryName;
            return appliedQuery;
        }
    }

    private String baseQueryName;

    private AppliedDatabaseQuery() {
    }

    /**
     * Gets the name of the underlying {@link org.tablevert.core.config.DatabaseQuery} object.
     *
     * @return the base query name
     */
    @Override
    public String getBaseQueryName() {
        return baseQueryName;
    }

}
