package org.crud2.jdbc;

import lombok.Data;

import java.io.Serializable;

@Data
public class DataRow implements Serializable {
    private Object[] itemArray;

    public DataRow() {
    }

    public DataRow(int columnCount) {
        itemArray = new Object[columnCount];
    }

    public void set(int index, Object value) {
        itemArray[index] = value;
    }

    public Object get(int index) {
        return itemArray[index];
    }
}
