package org.crud2.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;

public class ExColumnMapRowMapper extends ColumnMapRowMapper {
    @Autowired
    private ColumnKeyNameResolver columnKeyNameResolver;

    ExColumnMapRowMapper() {

    }

    ExColumnMapRowMapper(ColumnKeyNameResolver columnKeyNameResolver) {
        this.columnKeyNameResolver = columnKeyNameResolver;
    }

    @Override
    protected String getColumnKey(String columnName) {
        if (columnKeyNameResolver == null) return columnName;
        String reName = columnKeyNameResolver.resolve(columnName);
        return reName == null ? columnName : reName;
    }
}
