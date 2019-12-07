package org.crud2.core.edit;

import javax.sql.DataSource;
import java.util.Map;

public interface Update {
    Update setDataSource(DataSource dataSource);

    Update table(String table);

    Update sql(String sql);

    Update set(Map<String, Object> values);

    Update set(String field, Object value);

    Update byKey(String key, Object value);

    Update byId(Object value);

    Update where(String field, String oper, Object value);

    void flush();
}
