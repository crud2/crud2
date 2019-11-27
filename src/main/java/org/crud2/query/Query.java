package org.crud2.query;

import org.crud2.jdbc.DataTable;
import org.crud2.query.result.PagerResult;

import java.util.List;
import java.util.Map;

public interface Query {
    QueryParameter toParameter();

    Query from(String tableName);

    Query select(String fields);

    Query select(String... fields);

    Query sql(String sqlText);

    Query pageSizeIndex(int size, int index);

    Query pageOffsetLimit(int offset, int limit);

    Query where(String field, String dbOperator, Object value);

    Where where(String field);

    List queryList();

    DataTable queryDataTable();

    List<Map<String, Object>> queryListMap();

    <T> T get();

    Map<String, Object> getMap();

    <T> PagerResult<T> queryPager();

    PagerResult<List<Map<String, Object>>>  queryListMapPager();

    PagerResult<DataTable> queryDataTablePager();
}
