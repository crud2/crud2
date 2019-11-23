package org.crud2.edit.impl;

import org.crud2.edit.EditParameter;
import org.crud2.edit.Insert;
import org.crud2.db.EditDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope("prototype")
public class InsertImpl implements Insert {
    private EditParameter parameter;

    @Autowired
    private EditDAO editDao;

    public InsertImpl() {
        parameter = new EditParameter();
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
    public Object flush() {
        if (parameter.isIdentity()) {
            return editDao.insertIdentity(parameter);
        } else {
            parameter.setIdentity(false);
            return null;
        }
    }

    private void fixKeyInsertValue() {
        if (parameter.getValues().containsKey(parameter.getKey())) {
            parameter.setKeyValue(parameter.getValues().get(parameter.getKey()));
        }
    }
}
