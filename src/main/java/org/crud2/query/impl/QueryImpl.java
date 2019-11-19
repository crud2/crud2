package org.crud2.query.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.crud2.mybatis.dao.QueryDao;
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

@Component
@Scope("prototype")
public class QueryImpl implements Query {

    private QueryParameter parameter;

    @Autowired
    private QueryDao queryDao;
    @Autowired
    private ApplicationContext context;
    @Autowired
    private OperatorFactory operatorFactory;
    @Autowired
    private MapResultKeyNameResolver mapResultKeyNameResolver;

    public QueryImpl() {
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

    @Override
    public List<Map<String, Object>> queryMapList() {
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
    public PagerResult<Map<String, Object>> queryListMapPager() {
        PagerResult<Map<String, Object>> result = innerQueryMapPager();
        List<Map<String, Object>> data = result.getData();
        if (data.size() > 0L) {
            /*
             * set result map key name by MapResultKeyNameResolver
             * OrignalMapResultKeyNameResolver means not change
             */
            {
                if (mapResultKeyNameResolver instanceof OrignalMapResultKeyNameResolver) {
                    result.setData(data);
                } else {
                    Map<String, String> nameMap = getKeyNameMap(data);
                    List<Map<String, Object>> tempData = new ArrayList<>();
                    data.forEach(m -> {
                        Map<String, Object> rm = new HashMap<>();
                        m.forEach((k, v) -> {
                            rm.put(nameMap.get(k), m.get(k));
                        });
                        tempData.add(rm);
                    });
                    result.setData(tempData);
                }
            }
        }
        return result;
    }

    @Override
    public PagerArrayListResult queryArraylistPager() {
        PagerArrayListResult result = new PagerArrayListResult();
        ArrayListData data = new ArrayListData();
        PagerResult<Map<String, Object>> mapPagerResult = innerQueryMapPager();
        {
            result.setPageNum(mapPagerResult.getPageNum());
            result.setPages(mapPagerResult.getPages());
            result.setPageSize(mapPagerResult.getPageSize());
            result.setTotal(mapPagerResult.getTotal());
        }
        List<Map<String, Object>> listMapData = mapPagerResult.getData();
        if (listMapData.size() > 0L) {
            if (mapResultKeyNameResolver instanceof OrignalMapResultKeyNameResolver) {
                data.parse(listMapData);
            } else {
                Map<String, String> nameMap = getKeyNameMap(listMapData);
                data.parse(nameMap, listMapData);
            }
        }
        return result;
    }


    private PagerResult<Map<String, Object>> innerQueryMapPager() {
        switch (parameter.getPageType()) {
            case QueryParameter.PAGE_TYPE_SIZE_INDEX:
                PageHelper.startPage(parameter.getPageIndex(), parameter.getPageSize());
                break;
            case QueryParameter.PAGE_TYPE_OFFSET_LIMIT:
                PageHelper.offsetPage(parameter.getOffset(), parameter.getLimit());
                break;
        }
        List<Map<String, Object>> data = queryDao.query(parameter);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(data);
        PagerResult<Map<String, Object>> result = new PagerResult<>();
        //resolve result pageinfo
        {
            result.setTotal(pageInfo.getTotal());
            result.setPages(pageInfo.getPages());
            result.setPageSize(pageInfo.getPageSize());
            result.setPageNum(pageInfo.getPageNum());
        }
        result.setData(data);
        return result;
    }

    private Map<String, String> getKeyNameMap(List<Map<String, Object>> data) {
        Map<String, Object> item0 = data.get(0);
        Map<String, String> nameMap = new HashMap<>();
        item0.keySet().forEach(k -> {
            nameMap.put(k, mapResultKeyNameResolver.resolve(k));
        });
        return nameMap;
    }
}
