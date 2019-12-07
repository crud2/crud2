package org.crud2.core.edit.impl;

import org.crud2.core.edit.Delete;
import org.crud2.core.edit.EditParameter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

public abstract class AbstractDeleteImpl implements Delete {
    protected EditParameter parameter;

    @Autowired
    protected DataSource dataSource;

    public AbstractDeleteImpl() {
        parameter = new EditParameter();
    }

    @Override
    public Delete setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    @Override
    public Delete from(String table) {
        parameter.setTable(table);
        return this;
    }

    @Override
    public Delete byKey(String key, Object value) {
        parameter.setKey(key);
        parameter.setKeyValue(value);
        return this;
    }

    @Override
    public Delete byId(Object value) {
        parameter.setKey("id");
        parameter.setKeyValue(value);
        return this;
    }

    @Override
    public Delete where(String field, String oper, Object value) {
        return this;
    }

    @Override
    public abstract void flush();
}
