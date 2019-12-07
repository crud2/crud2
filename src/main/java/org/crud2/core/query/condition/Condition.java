package org.crud2.core.query.condition;

import lombok.Data;

@Data
public class Condition {
    private String field;
    private String oper;
    private Object value;
}
