package org.crud2.query.impl.inner;

import org.crud2.jdbc.DataTable;
import org.crud2.jdbc.PreparedSQLCommand;
import org.crud2.jdbc.PreparedSQLCommandBuilder;
import org.crud2.jdbc.SQLContext;
import org.crud2.query.QueryParameter;
import org.crud2.query.condition.Condition;
import org.crud2.query.impl.AbstractQueryImpl;
import org.crud2.query.result.PagerResult;
import org.crud2.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class InnerQueryImpl extends AbstractQueryImpl {

    private static Logger logger = LoggerFactory.getLogger(InnerQueryImpl.class);

    @Autowired
    SQLContext sqlContext;

    private PreparedSQLCommand countCommand;
    private PreparedSQLCommand dataCommand;

    @Override
    public List queryList() {
        return null;
    }

    @Override
    public DataTable queryDataTable() {
        buildQueryCommand();
        return sqlContext.queryDataTable(dataCommand);
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
    public PagerResult<List<Map<String, Object>>> queryListMapPager() {
        buildQueryPagerCommand();
        long total = sqlContext.queryForLong(countCommand);
        List<Map<String, Object>> data = sqlContext.queryForMapList(dataCommand);
        PagerResult<List<Map<String, Object>>> result = new PagerResult<>();
        setResultPagerInfo(result, total);
        result.setData(data);
        return result;
    }

    @Override
    public PagerResult<DataTable> queryDataTablePager() {
        buildQueryPagerCommand();
        long total = sqlContext.queryForLong(countCommand);
        DataTable data = sqlContext.queryDataTable(dataCommand);
        PagerResult<DataTable> result = new PagerResult<>();
        setResultPagerInfo(result, total);
        result.setData(data);
        return result;
    }

    private void buildQueryCommand() {
        PreparedSQLCommandBuilder dataQueryBuilder = PreparedSQLCommandBuilder.newInstance();
        if (StringUtil.isNullOrEmpty(parameter.getSql()) && StringUtil.isNullOrEmpty(parameter.getQueryTable())) {
            logger.error("query-sql and query-table can not be all null");
            return;
        }
        if (isUseSqlQuery()) {
            dataQueryBuilder.append(parameter.getSql());
        } else {
            dataQueryBuilder.append("SELECT ");
            if (parameter.getQueryFields() != null && parameter.getQueryFields().length != 0) {
                dataQueryBuilder.append(parameter.getQueryFields());
            } else {
                dataQueryBuilder.append("*");
            }
            dataQueryBuilder.append(" FROM %s ", parameter.getQueryTable());
        }
        buildConditionCommand(dataQueryBuilder, null);
        dataCommand = dataQueryBuilder.build();
    }

    private void buildQueryPagerCommand() {
        PreparedSQLCommandBuilder dataQueryBuilder = PreparedSQLCommandBuilder.newInstance();
        PreparedSQLCommandBuilder countQueryBuilder = PreparedSQLCommandBuilder.newInstance();
        if (StringUtil.isNullOrEmpty(parameter.getSql()) && StringUtil.isNullOrEmpty(parameter.getQueryTable())) {
            logger.error("query-sql and query-table can not be all null");
            return;
        }
        if (isUseSqlQuery()) {
            dataQueryBuilder.append(parameter.getSql());
            countQueryBuilder.append("SELECT COUNT(1) FROM ( %s", parameter.getSql());
            buildConditionCommand(dataQueryBuilder, countQueryBuilder);
            countQueryBuilder.append(") _p");
        } else {
            dataQueryBuilder.append("SELECT ");
            countQueryBuilder.append("SELECT COUNT(1) FROM %s ", parameter.getQueryTable());
            if (parameter.getQueryFields() != null && parameter.getQueryFields().length != 0) {
                dataQueryBuilder.append(parameter.getQueryFields());
            } else {
                dataQueryBuilder.append("*");
            }
            dataQueryBuilder.append(" FROM %s ", parameter.getQueryTable());
            buildConditionCommand(dataQueryBuilder, countQueryBuilder);
        }
        buildConditionCommand(dataQueryBuilder, countQueryBuilder);
        buildPagerCommand(dataQueryBuilder);
        dataCommand = dataQueryBuilder.build();
        countCommand = countQueryBuilder.build();
    }

    private void buildConditionCommand(PreparedSQLCommandBuilder dataQueryBuilder, PreparedSQLCommandBuilder countQueryBuilder) {
        if (parameter.getConditions() != null && parameter.getConditions().size() > 0) {
            List<Condition> conditions = parameter.getConditions();
            if (!(isUseSqlQuery() && parameter.getSql().toUpperCase().contains(" WHERE "))) {
                dataQueryBuilder.append(" WHERE ");
                if (countQueryBuilder != null) countQueryBuilder.append(" WHERE ");
            }
            conditions.forEach(t -> {
                dataQueryBuilder.append(" %s %s ", t.getField(), t.getOper());
                if (countQueryBuilder != null) countQueryBuilder.append(" %s %s ", t.getField(), t.getOper());
                switch (t.getOper()) {
                    case "in":
                    case "not in":
                        dataQueryBuilder.append("(");
                        if (countQueryBuilder != null) countQueryBuilder.append("(");
                        Object[] values = (Object[]) t.getValue();
                        dataQueryBuilder.appendPlaceolder(values.length);
                        if (countQueryBuilder != null) countQueryBuilder.appendPlaceolder(values.length);
                        dataQueryBuilder.append(")");
                        if (countQueryBuilder != null) countQueryBuilder.append(")");
                        dataQueryBuilder.appendParam(t.getField(), values);
                        if (countQueryBuilder != null) countQueryBuilder.appendParam(t.getField(), values);
                        break;
                    case "is null":
                    case "is not null":
                        break;
                    default:
                        dataQueryBuilder.appendPlaceholder();
                        if (countQueryBuilder != null) countQueryBuilder.appendPlaceholder();
                        dataQueryBuilder.appendParam(t.getOper(), t.getValue());
                        if (countQueryBuilder != null) countQueryBuilder.appendParam(t.getOper(), t.getValue());
                        break;
                }
            });
        }
    }

    private void buildPagerCommand(PreparedSQLCommandBuilder dataQueryBuilder) {
        if (!parameter.isPageConfiged()) return;
        //TODO:muti dialect
        if (QueryParameter.PAGE_TYPE_SIZE_INDEX.equals(parameter.getPageType())) {
            calculateOffsetLimit();
        }
        dataQueryBuilder.append(" LIMIT ?");
        dataQueryBuilder.appendParam("limit", parameter.getLimit());
        if (parameter.getOffset() > 0) {
            dataQueryBuilder.append(" OFFSET ?");
            dataQueryBuilder.appendParam("offset", parameter.getOffset());
        }
    }

    private void calculateOffsetLimit() {
        parameter.setLimit(parameter.getPageSize());
        parameter.setOffset((parameter.getPageIndex() - 1) * parameter.getPageSize());
    }

    private void calculatePageSize() {
        if (parameter.getOffset() % parameter.getLimit() == 0) {
            parameter.setPageIndex(parameter.getOffset() / parameter.getLimit() + 1);
            parameter.setPageSize(parameter.getLimit());
        } else {
            logger.debug("limit offset is not match,can not calculate to page-size-index");
        }
    }

    private void setResultPagerInfo(PagerResult result, long total) {
        if (QueryParameter.PAGE_TYPE_OFFSET_LIMIT.equals(parameter.getPageType())) {
            calculatePageSize();
        }
        result.setTotal(total);
        result.setPages((int) (total % parameter.getLimit() == 0 ? (total / parameter.getLimit()) : (total / parameter.getLimit() + 1)));
        result.setPageSize(parameter.getLimit());
        result.setPageNum(parameter.getPageIndex());
    }

    private boolean isUseSqlQuery() {
        return !StringUtil.isNullOrEmpty(parameter.getSql());
    }
}
