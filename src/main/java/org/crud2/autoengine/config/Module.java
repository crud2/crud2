package org.crud2.autoengine.config;

import lombok.Data;

@Data
public class Module {
    private String id;
    private String editTable;
    private String sql;
    private Column[] columns;

    public Column getKey() {
        for (Column column : columns) {
            if (column.getKey() == 1) {
                return column;
            }
        }
        return null;
    }

    public Column getColumn(String name) {
        for (Column column : columns) {
            if (column.getName().equals(name)) return column;
        }
        return null;
    }
}
