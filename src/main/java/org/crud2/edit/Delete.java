package org.crud2.edit;

public interface Delete {
    Delete from(String table);

    Delete byKey(String key, Object value);

    Delete byId(Object value);

    Delete where(String field, String oper, Object value);

    void flush();
}
