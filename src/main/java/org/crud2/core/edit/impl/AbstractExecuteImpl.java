package org.crud2.core.edit.impl;

import org.crud2.core.edit.EditParameter;
import org.crud2.core.edit.Execute;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;

public abstract class AbstractExecuteImpl implements Execute {
    protected EditParameter parameter;

    @Autowired
    protected DataSource dataSource;

    public AbstractExecuteImpl() {
        parameter = new EditParameter();
    }

    @Override
    public Execute setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    @Override
    public Execute sql(String sql) {
        parameter.setSql(sql);
        return this;
    }

    @Override
    public abstract void flush();
}
