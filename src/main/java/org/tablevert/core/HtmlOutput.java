/*
 * Copyright 2019 conis Informationssysteme GmbH
 * SPDX-License-Identifier: Apache-2.0
 */

package org.tablevert.core;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class HtmlOutput implements Output {

    static final class Builder {
        private final StringBuilder table;
        private List<Column> columns;
        private List<DataRow> rows;

        Builder() {
            table = new StringBuilder();
        }

        Builder withColumns(List<Column> colsToAdd) {
            this.columns = colsToAdd;
            return this;
        }

        Builder withRows(List<DataRow> rowsToAdd) {
            this.rows = rowsToAdd;
            return this;
        }

        HtmlOutput fullTable() throws BuilderFailedException {
            validate();
            startTable();
            writeColumns();
            writeBody();
            endTable();
            return new HtmlOutput(table.toString());
        }

        private void validate() throws BuilderFailedException {
            StringBuilder errors = new StringBuilder();
            if (columns == null) {
                errors.append(" - columns not specified;");
            }
            else if (columns.isEmpty()) {
                errors.append(" - columns empty;");
            }
            if (errors.length() > 0) {
                throw new BuilderFailedException("Builder validation failed with errors: " + errors.toString());
            }
        }

        private void startTable() {
            table.append("<table>");
        }

        private void writeColumns() {
            table.append("<colgroup>");
            table.append("<col class=\"tv-col-id\">").append("</col>");
            columns.forEach(this::addColumnDefinition);
            table.append("</colgroup>");
            table.append("<thead><tr>");
            columns.forEach(this::addColumnHeader);
            table.append("</tr></thead>");
        }

        private void writeBody() {
            table.append("<tbody>");
            rows.forEach(this::addDataRow);
            table.append("</tbody>");
        }

        private void addColumnDefinition(Column column) {
            table.append("<col>");
            table.append("</col>");
        }

        private void addColumnHeader(Column column) {
            table.append("<th>");
            table.append(column.header);
            table.append("</th>");
        }

        private void addDataRow(DataRow dataRow) {
            table.append("<tr>");
            if (dataRow != null) {
                table.append(String.format("<td>%s</td>", dataRow.id));
                dataRow.values.forEach(this::addDataCell);
            }
            table.append("</tr>");
        }

        private void addDataCell(String value) {
            table.append("<td>");
            if (value != null) {
                table.append(value);
            }
            table.append("</td>");
        }

        private void endTable() {
            table.append("</table>");
        }

    }

    static final class Column {
        private final String id;
        private final String header;
        private final HtmlOutputCssClass cssClass;

        Column(String id, String header) {
            this(id, header, HtmlOutputCssClass.TV_NONE);
        }

        Column(String id, String header, HtmlOutputCssClass cssClass) {
            this.id = id;
            this.header = header;
            this.cssClass = cssClass;
        }
    }

    static final class DataRow {
        private final String id;
        private final HtmlOutputCssClass cssClass;
        private List<String> values;

        DataRow(String id) {
            this(id, HtmlOutputCssClass.TV_NONE);
        }

        DataRow(String id, HtmlOutputCssClass cssClass) {
            this.id = id;
            this.cssClass = cssClass;
            this.values = new ArrayList<>();
        }

        void addValue(String value) {
            values.add(value);
        }
    }

    private String content;

    private HtmlOutput(String content) {
        this.content = content;
    }

    @Override
    public OutputFormat getFormat() {
        return OutputFormat.HTML;
    }

    @Override
    public void writeContent(OutputStream outputStream) throws OutputGeneratorException {
        try {
            outputStream.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new OutputGeneratorException("Failed to write content to output stream");
        }
    }

    @Override
    public String toString() {
        return content;
    }

}
