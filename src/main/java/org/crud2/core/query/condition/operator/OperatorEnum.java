package org.crud2.core.query.condition.operator;

public enum OperatorEnum {
    Equal("eq"),
    NotEqual("ne"),
    Contains("cn");

    private final String operator;

    OperatorEnum(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return this.operator;
    }
}
