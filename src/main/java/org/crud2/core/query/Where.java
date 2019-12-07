package org.crud2.core.query;

public interface Where {
    Query equal(Object value);

    Query notEqual(Object value);

    Query contains(String value);
}
