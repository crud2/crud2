package org.crud2.edit.impl.mybatis;

import org.crud2.db.EditDAO;
import org.crud2.edit.impl.AbstractUpdateImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class MyBatisUpdateImpl extends AbstractUpdateImpl {
    @Autowired
    private EditDAO editDao;

    @Override
    public void flush() {
        editDao.update(parameter);
    }
}
