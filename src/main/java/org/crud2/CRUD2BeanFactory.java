package org.crud2;

import org.crud2.edit.Delete;
import org.crud2.edit.Insert;
import org.crud2.edit.Update;
import org.crud2.edit.impl.DeleteImpl;
import org.crud2.query.Query;
import org.crud2.query.Where;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn("curd2initializer")
public class CRUD2BeanFactory {
    @Autowired
    private ApplicationContext context;

    public Query getQuery() {
        return context.getBean(Query.class);
    }

    public Where getWhere() {
        return context.getBean(Where.class);
    }

    public Insert getInsert() {
        return context.getBean(Insert.class);
    }

    public Update getUpdate() {
        return context.getBean(Update.class);
    }

    public Delete getDelete() {
        return context.getBean(Delete.class);
    }
}