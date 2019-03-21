/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import java.util.Hashtable;
import java.util.Map;

/**
 * Simple implementation of the {@link TablevertConfig} interface.
 */
public class SimpleTablevertConfig implements TablevertConfig {

    private final Map<String, DatabaseQuery> queryMap;

    /**
     * Builder for SimpleTablevertConfig instances.
     */
    public static class Builder {

        private SimpleTablevertConfig config;

        public Builder withQuery(DatabaseQuery query) {
            config.queryMap.replace(query.getName(), query);
            return this;
        }


        public SimpleTablevertConfig build() {
            return config;
        }
    }

    private SimpleTablevertConfig() {
        this.queryMap = new Hashtable<>();
    }
}
