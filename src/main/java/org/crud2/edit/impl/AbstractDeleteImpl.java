package org.crud2.edit.impl;

import org.crud2.edit.Delete;
import org.crud2.edit.EditParameter;
import org.crud2.db.EditDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class AbstractDeleteImpl implements Delete {
    private EditParameter parameter;
    @Autowired
    private EditDAO editDao;

    public AbstractDeleteImpl() {
        parameter = new EditParameter();
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
    public void flush() {
        editDao.delete(parameter);
    }
}
