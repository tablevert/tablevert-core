/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core.config;

import java.util.Objects;

/**
 * A single column within a database query.
 */
public class DatabaseQueryColumn implements Cloneable {

    private String formula;
    private String name;

    /**
     * Creates the column based on a calculation formula.
     * @param formula calculation formula of the column value
     * @param name the name by which the column is referenced in the query result
     */
    public DatabaseQueryColumn(String formula, String name) {
        this.formula = formula;
        this.name = name;
    }


    /**
     * Creates the column just using a name.
     * @param name the name by which the column is referenced in the query result
     */
    public DatabaseQueryColumn(String name) {
        this.formula = null;
        this.name = name;
    }

    /**
     * Gets the calculation formula by which the column value is derived.
     * @return the formula
     */
    public String getFormula() {
        return formula;
    }

    /**
     * Gets the name by which the column is referenced in the query result.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Creates a deep clone of this object.
     * @return the clone
     */
    @Override
    public DatabaseQueryColumn clone() {
        return new DatabaseQueryColumn(this.formula, this.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!DatabaseQueryColumn.class.equals(obj.getClass())) {
            return false;
        }
        DatabaseQueryColumn queryColumn = (DatabaseQueryColumn)obj;
        return Objects.equals(queryColumn.formula, this.formula)
                && Objects.equals(queryColumn.name, this.name);
    }

}
