package org.crud2.core.edit.impl;

import org.crud2.core.edit.EditParameter;
import org.crud2.core.edit.Insert;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.util.Map;


public abstract class AbstractInsertImpl implements Insert {
    protected EditParameter parameter;
    @Autowired
    protected DataSource dataSource;
    public AbstractInsertImpl() {
        parameter = new EditParameter();
    }

    @Override
    public Insert setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    @Override
    public Insert into(String table) {
        parameter.setTable(table);
        return this;
    }

    @Override
    public Insert values(Map<String, Object> values) {
        parameter.setValues(values);
        if (parameter.isIdentity()) fixKeyInsertValue();
        return this;
    }

    @Override
    public Insert value(String field, Object value) {
        parameter.getValues().put(field, value);
        if (parameter.isIdentity()) fixKeyInsertValue();
        return this;
    }

    @Override
    public Insert identity(String key) {
        parameter.setKey(key);
        parameter.setIdentity(true);
        fixKeyInsertValue();
        return this;
    }

    @Override
    public abstract Object flush();

    private void fixKeyInsertValue() {
        if (parameter.getValues().containsKey(parameter.getKey())) {
            parameter.setKeyValue(parameter.getValues().get(parameter.getKey()));
        }
    }
}
