package org.crud2.jdbc;

import lombok.Data;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

@Data
public class DataTableRowMapper implements RowMapper<DataRow> {

    /**
     * Columns we've seen so far
     */
    private int columnCount;

    /**
     * Indexed from 0. Type (as in java.sql.Types) for the columns
     * as returned by ResultSetMetaData object.
     */
    @Nullable
    private int[] columnTypes;

    /**
     * Indexed from 0. Column name as returned by ResultSetMetaData object.
     */
    @Nullable
    private String[] columnNames;

    @Override
    public DataRow mapRow(ResultSet rs, int rowNum) throws SQLException {
        if (rowNum == 0) {
            ResultSetMetaData rsmd = rs.getMetaData();
            this.columnCount = rsmd.getColumnCount();
            this.columnTypes = new int[this.columnCount];
            this.columnNames = new String[this.columnCount];
            for (int i = 0; i < this.columnCount; i++) {
                this.columnTypes[i] = rsmd.getColumnType(i + 1);
                this.columnNames[i] = JdbcUtils.lookupColumnName(rsmd, i + 1);
            }
        }
        DataRow row = new DataRow(columnCount);
        for (int i = 0; i < columnCount; i++) {
            row.set(i, rs.getObject(i));
        }
        return row;
    }
}
