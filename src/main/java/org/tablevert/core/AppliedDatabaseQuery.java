/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

public class AppliedDatabaseQuery implements AppliedQuery {
    private String baseQueryName;

    public static class Builder {
        private final String baseQueryName;

        public Builder(String baseQueryName) {
            this.baseQueryName = baseQueryName;
        }

        public AppliedDatabaseQuery build() {
            AppliedDatabaseQuery appliedQuery = new AppliedDatabaseQuery();
            appliedQuery.baseQueryName = this.baseQueryName;
            return appliedQuery;
        }
    }

    public String getBaseQueryName() {
        return baseQueryName;
    }
}
