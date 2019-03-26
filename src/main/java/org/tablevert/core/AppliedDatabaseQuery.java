/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

/**
 * A database query, where the underlying {@link org.tablevert.core.config.DatabaseQuery} instance is extended with
 * context-specific filtering and sorting.
 */
public class AppliedDatabaseQuery {

    /**
     * Builder for {@link AppliedDatabaseQuery} instances.
     */
    public static class Builder {
        private String baseQueryName;
        private String userName;

        /**
         * Sets the {@link org.tablevert.core.config.DatabaseQuery} reference.
         * @param baseQueryName the name of the underlying base query
         * @return the builder
         */
        public Builder forBaseQuery(String baseQueryName) {
            this.baseQueryName = baseQueryName;
            return this;
        }

        /**
         * Sets the name of the database user to apply.
         * @param userName the database user name
         * @return the builder
         */
        public Builder withUser(String userName) {
            this.userName = userName;
            return this;
        }

        /**
         * Builds the {@link AppliedDatabaseQuery} object.
         * @return the built object
         */
        public AppliedDatabaseQuery build() {
            // TODO: Add validation
            AppliedDatabaseQuery appliedQuery = new AppliedDatabaseQuery();
            appliedQuery.baseQueryName = this.baseQueryName;
            appliedQuery.userName = this.userName;
            return appliedQuery;
        }
    }

    private String baseQueryName;
    private String userName;

    private AppliedDatabaseQuery() {
    }

    /**
     * Gets the name of the underlying {@link org.tablevert.core.config.DatabaseQuery} object.
     * @return the base query name
     */
    public String getBaseQueryName() {
        return baseQueryName;
    }

    /**
     *
     * @return the name of the database user to apply to this query
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Extends the base query with the filter and sort criteria of the applied query.
     * @return the effective query
     */
    public String getEffectiveQuery() {
        return null;
    }
}
