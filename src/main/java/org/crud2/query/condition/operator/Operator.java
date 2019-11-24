package org.crud2.query.condition.operator;

import org.crud2.query.condition.Condition;

@FunctionalInterface
public interface Operator {
    default String getName() {
        return "";
    }

    void resolveCondition(Condition condition, String operName, Object value);
}
