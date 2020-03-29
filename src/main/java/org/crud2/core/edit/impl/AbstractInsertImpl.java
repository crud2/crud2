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
        return this;
    }

    @Override
    public Insert value(String field, Object value) {
        parameter.getValues().put(field, value);
        return this;
    }

    @Override
    public Insert useGenerateKey(String key) {
        parameter.setUseGenerateKey(true);
        return this;
    }

    @Override
    public Insert useSelectKey(String key, String selectKeySql) {
        parameter.setUseSelectKey(true);
        parameter.setKey(key);
        parameter.setSelectKeySql(selectKeySql);
        return this;
    }

    @Override
    public abstract Object flush();
}
