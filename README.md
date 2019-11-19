# CRUD2 project
A not only ORM framework based on mybatis
## ntroduce
- A simpler method is provided to realize the operation of query, insert, delete and update.
- The goal of the project is to simplify the process complexity. Programmers focus on business and data model design, free from object-oriented programming and multiple design patterns.

# Features

# Getting started
> it is recommended to use spring boot starter to introduce
### Maven dependency
``` xml
<dependency>
    <groupId>org.crud2.spring.boot</groupId>
    <artifactId>crud2-spring-boot-starter</artifactId>
    <version>0.0.1</version>
</dependency>
```
### Query
#### basic use
```java
CRUD.query()
	.from(String tableName)
	.select(String ... fields)
	.pageSizeIndex(int pageSize,pageIndex)
	.where(String fieldName,String oper,Object value)
	.queryMapPager()
```
#### page params
```java
query.pageSizeIndex(int pageSize,int pageIndex)
query.pageOffsetLimit(int offset,int limit)
```
### query result
by default,query can return a map<string,Object> or a arraylist data.

You can customize the keyname of the returned map,
by default, the keyname will be converted to lowercase(LowerCaseMapResultKeyNameResolver)
```java
query.queryMapPager() // return a List<Map<String,Object>>
query.queryArraylistPager()
```
#### Whwere condition

### Insert
### Update
### Delete


# Auto Engine

# License
CRUD2 is under the Apache 2.0 license. See the LICENSE file for details.

**important** : 
This project is not completed, please do not use it in the production environment
