package org.crud2.autoengine;

import org.crud2.core.query.Query;

@FunctionalInterface
public interface BeforeQueryHander {
    void handle(Query query);
}
