package org.crud2.edit.impl;

import org.crud2.edit.EditParameter;
import org.crud2.edit.Update;
import org.crud2.db.EditDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


public abstract class AbstractUpdateImpl implements Update {
    protected EditParameter parameter;

    public AbstractUpdateImpl() {
        parameter = new EditParameter();
    }

    @Override
    public Update table(String table) {
        parameter.setTable(table);
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
