package org.crud2.core.edit;

import javax.sql.DataSource;

public interface Execute {
    Execute setDataSource(DataSource dataSource);
    Execute sql(String sql);
    void flush();
}
