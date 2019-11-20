package org.crud2.edit;

import java.util.Map;

public interface Insert {
    Insert into(String table);

    Insert values(Map<String, Object> values);

    Insert value(String field, Object value);

    Insert identity();

    Object flush();
}
