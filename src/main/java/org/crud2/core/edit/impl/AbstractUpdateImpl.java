package org.crud2.core.edit.impl;

import org.crud2.core.edit.EditParameter;
import org.crud2.core.edit.Update;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;


public abstract class AbstractUpdateImpl implements Update {
    protected EditParameter parameter;
    @Autowired
    protected DataSource dataSource;

    public AbstractUpdateImpl() {
        parameter = new EditParameter();
    }

    @Override
    public Update setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    @Override
    public Update table(String table) {
        parameter.setTable(table);
        return this;
    }

    @Override
    public Update sql(String sql) {
        parameter.setSql(sql);
        return this;
    }

    @Override
    public Update set(Map<String, Object> values) {
        parameter.setValues(values);
        return this;
    }

    @Override
    public Update set(String field, Object value) {
        if (parameter.getValues() == null) {
            parameter.setValues(new HashMap<>());
        }
        parameter.getValues().put(field, value);
        return this;
    }

    @Override
    public Update byKey(String keyField, Object value) {
        parameter.setKey(keyField);
        parameter.setKeyValue(value);
        return this;
    }

    @Override
    public Update byId(Object value) {
        parameter.setKey("id");
        parameter.setKeyValue(value);
        return this;
    }


    @Override
    public Update where(String field, String oper, Object value) {
        return this;
    }

    @Override
    public abstract void flush();
}
