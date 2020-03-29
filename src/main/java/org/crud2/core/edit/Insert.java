package org.crud2.core.edit;

import javax.sql.DataSource;
import java.util.Map;

public interface Insert {
    Insert setDataSource(DataSource dataSource);

    Insert into(String table);

    Insert values(Map<String, Object> values);

    Insert value(String field, Object value);

    Insert useGenerateKey(String key);

    Insert useSelectKey(String key,String selectKeySql);

    Object flush();
}
