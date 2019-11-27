package org.crud2.autoengine.listsource;

import org.crud2.jdbc.DataRow;
import org.crud2.jdbc.DataTable;
import org.crud2.query.Query;
import org.crud2.util.KeyValuePair;
import org.crud2.util.RepeatableLinkedMap;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/***
 * sql list source,use first two field to map
 * support sql text parameter resolver
 * eg source:select text,value from dictionary
 * @see: SqlTextParameterResolver
 */
public class SqlListSourceParse implements ListSourceParse {
    @Autowired
    private Query query;

    @Override
    public RepeatableLinkedMap<String, Object> parse(String source, String valueType) {
        DataTable dataTable = query.sql(source).queryDataTable();
        RepeatableLinkedMap<String, Object> result = new RepeatableLinkedMap<>();
        for (int i = 0; i < dataTable.getRows().length; i++) {
            DataRow row = dataTable.getRow(i);
            result.put(row.get(0).toString(), row.get(1));
        }
        return result;
    }
}
