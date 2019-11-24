package org.crud2.edit;

import java.util.Map;

public interface Update {
    Update table(String table);
    Update set(Map<String,Object> values);
    Update set(String field,Object value);
    Update byKey(String key,Object value);
    Update byId(Object value);
    Update where(String field,String oper,Object value);
    void flush();
}
