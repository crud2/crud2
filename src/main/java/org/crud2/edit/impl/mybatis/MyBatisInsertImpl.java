package org.crud2.edit.impl.mybatis;

import org.crud2.db.EditDAO;
import org.crud2.edit.impl.AbstractInsertImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class MyBatisInsertImpl extends AbstractInsertImpl {
    @Autowired
    private EditDAO editDao;

    @Override
    public Object flush() {
        if (parameter.isIdentity()) {
            return editDao.insertIdentity(parameter);
        } else {
            parameter.setIdentity(false);
            return null;
        }
    }
}
