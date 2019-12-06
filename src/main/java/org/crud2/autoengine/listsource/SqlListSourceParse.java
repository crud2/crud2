package org.crud2.autoengine.listsource;

import org.crud2.CRUD2BeanFactory;
import org.crud2.autoengine.sql.SqlTextParameterResolver;
import org.crud2.jdbc.DataRow;
import org.crud2.jdbc.DataTable;
import org.crud2.query.Query;
import org.crud2.util.KeyValuePair;
import org.crud2.util.RepeatableLinkedMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * sql list source,use first two field to map
 * support sql text parameter resolver
 * eg source:select text,value from dictionary
 * @see: SqlTextParameterResolver
 */
public class SqlListSourceParse implements ListSourceParse {
    @Autowired
    private CRUD2BeanFactory beanFactory;

    @Override
    public RepeatableLinkedMap<String, Object> parse(String source, String valueType, Map<String, Object> parameters) {
        SqlTextParameterResolver parameterResolver = beanFactory.getBean(SqlTextParameterResolver.class);
        DataTable dataTable = beanFactory.getQuery().sql(parameterResolver.resolve(source, parameters)).queryDataTable();
        RepeatableLinkedMap<String, Object> result = new RepeatableLinkedMap<>();
        for (int i = 0; i < dataTable.getRows().length; i++) {
            DataRow row = dataTable.getRow(i);
            result.put(row.get(0).toString(), row.get(1));
        }
        return result;
    }
}
