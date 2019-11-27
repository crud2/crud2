package org.crud2.query.impl;

import org.crud2.jdbc.ColumnKeyNameResolver;
import org.crud2.jdbc.DataTable;
import org.crud2.query.Query;
import org.crud2.query.QueryParameter;
import org.crud2.query.Where;
import org.crud2.query.condition.Condition;
import org.crud2.query.condition.OperatorFactory;
import org.crud2.query.condition.operator.Operator;
import org.crud2.query.result.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractQueryImpl implements Query {

    protected QueryParameter parameter;

    @Autowired
    private ApplicationContext context;
    @Autowired
    private OperatorFactory operatorFactory;
    @Autowired
    protected ColumnKeyNameResolver columnKeyNameResolver;

    public AbstractQueryImpl() {
        parameter = new QueryParameter();
        parameter.setConditions(new ArrayList<>());
    }

    public QueryParameter toParameter() {
        return parameter;
    }

    @Override
    public Query from(String tableName) {
        parameter.setQueryTable(tableName);
        return this;
    }

    @Override
    public Query select(String fields) {
        parameter.setQueryFields(fields.split(","));
        return this;
    }

    @Override
    public Query select(String... fields) {
        parameter.setQueryFields(fields);
        return this;
    }

    @Override
    public Query sql(String sqlText) {
        parameter.setSql(sqlText);
        return this;
    }

    @Override
    public Query pageSizeIndex(int size, int index) {
        parameter.setPageType(QueryParameter.PAGE_TYPE_SIZE_INDEX);
        parameter.setPageSize(size);
        parameter.setPageIndex(index);
        return this;
    }

    @Override
    public Query pageOffsetLimit(int offset, int limit) {
        parameter.setPageType(QueryParameter.PAGE_TYPE_OFFSET_LIMIT);
        parameter.setOffset(offset);
        parameter.setLimit(limit);
        return this;
    }

    @Override
    public Query where(String field, String operatorName, Object value) {
        Condition condition = new Condition();
        this.parameter.getConditions().add(condition);
        condition.setField(field);
        Operator operator = operatorFactory.getOperator(operatorName);
        if (operator == null) {
            condition.setOper(operatorName);
            condition.setValue(value);
        } else {
            operator.resolveCondition(condition, operatorName, value);
        }
        return this;
    }

    @Override
    public Where where(String field) {
        WhereImpl where = (WhereImpl) context.getBean(Where.class);
        where.setQuery(this);
        Condition condition = new Condition();
        this.parameter.getConditions().add(condition);
        condition.setField(field);
        where.setCondition(condition);
        return where;
    }

    @Override
    public List queryList() {
        return null;
    }

    @Autowired
    public abstract DataTable queryDataTable();

    @Override
    public List<Map<String, Object>> queryListMap() {
        return null;
    }

    @Override
    public <T> T get() {
        return null;
    }

    @Override
    public Map<String, Object> getMap() {
        return null;
    }

    @Override
    public <T> PagerResult<T> queryPager() {
        return null;
    }

    @Override
    public abstract PagerResult<List<Map<String, Object>>> queryListMapPager();

    @Override
    public abstract PagerResult<DataTable> queryDataTablePager();

    protected Map<String, String> getKeyNameMap(List<Map<String, Object>> data) {
        Map<String, Object> item0 = data.get(0);
        Map<String, String> nameMap = new HashMap<>();
        item0.keySet().forEach(k -> {
            nameMap.put(k, columnKeyNameResolver.resolve(k));
        });
        return nameMap;
    }
}
