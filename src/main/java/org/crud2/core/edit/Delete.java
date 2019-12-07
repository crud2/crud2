package org.crud2.core.edit;

import javax.sql.DataSource;

public interface Delete {
    Delete setDataSource(DataSource dataSource);

    Delete from(String table);

    Delete byKey(String key, Object value);

    Delete byId(Object value);

    Delete where(String field, String oper, Object value);

    void flush();
}
