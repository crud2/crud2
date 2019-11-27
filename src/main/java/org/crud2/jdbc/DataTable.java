package org.crud2.jdbc;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataTable implements Serializable {
    private String[] columns;
    private int[] columnTypes;
    private DataRow[] rows;

    public DataRow getRow(int i) {
        return rows[i];
    }
}
