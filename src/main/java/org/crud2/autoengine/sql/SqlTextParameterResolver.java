package org.crud2.autoengine.sql;

import java.util.Map;

@FunctionalInterface
public interface SqlTextParameterResolver {
    String resolve(String sql, Map<String,Object> params);
    default String[] getNames(String sql){
        return null;
    }
}
