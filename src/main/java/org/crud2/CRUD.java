package org.crud2;

import org.crud2.edit.Delete;
import org.crud2.edit.Insert;
import org.crud2.edit.Update;
import org.crud2.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CRUD {
    private static CRUD2BeanFactory beanFactory;

    public void setBeanFactory(CRUD2BeanFactory beanFactory){
        CRUD.beanFactory=beanFactory;
    }

    public static Query query() {
        return beanFactory.getQuery();
    }

    public static Insert insert() {
        return beanFactory.getInsert();
    }

    public static Update update() {
        return beanFactory.getUpdate();
    }

    public static Delete delete() {
        return beanFactory.getDelete();
    }
}
