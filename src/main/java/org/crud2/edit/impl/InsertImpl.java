package org.crud2.edit.impl;

import org.crud2.edit.EditParameter;
import org.crud2.edit.Insert;
import org.crud2.mybatis.dao.EditDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope("prototype")
public class InsertImpl implements Insert {
    private EditParameter parameter;

    @Autowired
    private EditDao editDao;

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
        return this;
    }

    @Override
    public Insert identity() {
        parameter.setIdentity(true);
        return this;
    }

    @Override
    public Object flush() {
        return editDao.insertWithAutoId(parameter);
    }
}
