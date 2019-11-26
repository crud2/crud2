package org.crud2.jdbc;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataTable implements Serializable {
    private String[] columns;
    public int[] columnTypes;
    public DataRow[] rows;
}
