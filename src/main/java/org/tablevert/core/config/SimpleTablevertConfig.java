/*
 * Copyright 2019 doc-hil
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

/**
 * Simple implementation of the {@link TablevertConfig} interface.
 */
public class SimpleTablevertConfig implements TablevertConfig {

    /**
     * Builder for SimpleTablevertConfig instances.
     */
    public static class Builder {

        private SimpleTablevertConfig config;


        public SimpleTablevertConfig build() {
            return config;
        }
    }

    private SimpleTablevertConfig() {

    }
}
