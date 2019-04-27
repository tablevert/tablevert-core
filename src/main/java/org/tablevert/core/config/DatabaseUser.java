/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

/**
 * A basic-auth user for database access.
 */
public final class DatabaseUser implements Cloneable {

    private String name;
    private String secret;

    public DatabaseUser(String name, String secret) {
        this.name = name;
        this.secret = secret;
    }

    /**
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    String getSecret() {
        return secret;
    }

    public DatabaseUser clone() {
        return new DatabaseUser(name, secret);
    }
}
