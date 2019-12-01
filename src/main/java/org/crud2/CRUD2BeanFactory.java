package org.crud2;

import org.crud2.edit.Delete;
import org.crud2.edit.Insert;
import org.crud2.edit.Update;
import org.crud2.edit.impl.inner.InnerDeleteImpl;
import org.crud2.edit.impl.inner.InnerInsertImpl;
import org.crud2.edit.impl.inner.InnerUpdateImpl;
import org.crud2.query.Query;
import org.crud2.query.Where;
import org.crud2.query.impl.inner.InnerQueryImpl;
import org.crud2.query.impl.mybatis.MyBatisQueryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;

@Component
@DependsOn("curd2initializer")
public class CRUD2BeanFactory {
    @Autowired
    private ApplicationContext context;

    public <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public Object getBean(String name) {
        return context.getBean(name);
    }

    public Query getQuery() {
        return context.getBean(InnerQueryImpl.class);
    }

    public Where getWhere() {
        return context.getBean(Where.class);
    }

    public Insert getInsert() {
        return context.getBean(InnerInsertImpl.class);
    }

    public Update getUpdate() {
        return context.getBean(InnerUpdateImpl.class);
    }

    public Delete getDelete() {
        return context.getBean(InnerDeleteImpl.class);
    }
}
