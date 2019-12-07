package org.crud2.autoengine;

import javax.sql.DataSource;

@FunctionalInterface
public interface AutoEngineSessionBean {
    DataSource getDataSource(String moduleId);
}