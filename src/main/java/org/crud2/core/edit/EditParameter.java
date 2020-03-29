package org.crud2.core.edit;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

public class EditParameter {
    private String table;
    private String sql;
    private String key;
    private Object keyValue;

    private boolean useGenerateKey = false;
    private boolean useSelectKey=false;
    private String selectKeySql;

    private Map<String, Object> values;

    public EditParameter() {
        values = new HashMap<>();
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(Object keyValue) {
        this.keyValue = keyValue;
    }

    public boolean useGenerateKey() {
        return useGenerateKey;
    }

    public void setUseGenerateKey(boolean useGenerateKey) {
        this.useGenerateKey = useGenerateKey;
    }

    public boolean useSelectKey() {
        return useSelectKey;
    }

    public void setUseSelectKey(boolean useSelectKey) {
        this.useSelectKey = useSelectKey;
    }

    public String getSelectKeySql() {
        return selectKeySql;
    }

    public void setSelectKeySql(String selectKeySql) {
        this.selectKeySql = selectKeySql;
    }

    public Map<String, Object> getValues() {
        return values;
    }

    public void setValues(Map<String, Object> values) {
        this.values = values;
    }

}
