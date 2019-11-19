# crud2
a not only orm framework based on mybatis

# basic use
> it is recommended to use spring boot starter to introduce
``` xml
        <dependency>
            <groupId>org.crud2.spring.boot</groupId>
            <artifactId>crud2-spring-boot-starter</artifactId>
            <version>0.0.1</version>
        </dependency>
```
``` java
CRUD.query()
	.from("tableName")
	.select("fields")
	.pageSizeIndex(20,1)
	.where(field,oper,value)
        .queryMapPager()
CRUD.insert()
CRUD.update()
CRUD.delete()
```

**important**
this project is not complete ,please do not use in your proc env
