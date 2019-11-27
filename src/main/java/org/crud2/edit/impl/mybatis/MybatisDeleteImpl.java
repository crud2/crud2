package org.crud2.edit.impl.mybatis;

import org.crud2.db.EditDAO;
import org.crud2.edit.Delete;
import org.crud2.edit.EditParameter;
import org.crud2.edit.impl.AbstractDeleteImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MybatisDeleteImpl extends AbstractDeleteImpl {
    @Autowired
    private EditDAO editDao;

    @Override
    public void flush() {
        editDao.delete(parameter);
    }
}
