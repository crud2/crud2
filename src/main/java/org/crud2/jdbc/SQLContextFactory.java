package org.crud2.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class SQLContextFactory {
    private Map<DataSource, SQLContext> dataSourceSQLContextMap = new HashMap<>();

    @Autowired
    private ColumnKeyNameResolver columnKeyNameResolver;

    public SQLContext getSQLContext(DataSource dataSource) {
        if (!dataSourceSQLContextMap.containsKey(dataSource)) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            SQLContext sqlContext = new SQLContext(jdbcTemplate, columnKeyNameResolver);
            dataSourceSQLContextMap.put(dataSource, sqlContext);
        }
        return dataSourceSQLContextMap.get(dataSource);
    }
}
