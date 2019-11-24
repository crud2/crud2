package org.crud2.query.impl;

import lombok.Data;
import org.crud2.query.Query;
import org.crud2.query.Where;
import org.crud2.query.condition.Condition;
import org.crud2.query.condition.OperatorFactory;
import org.crud2.query.condition.operator.Operator;
import org.crud2.query.condition.operator.OperatorEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Data
@Component
@Scope("prototype")
public class WhereImpl implements Where {

    private Condition condition;
    private Query query;

    @Autowired
    private OperatorFactory operatorFactory;

    private void resolveCondition(OperatorEnum operatorEnum, Object value) {
        Operator operator = operatorFactory.getOperator(operatorEnum.toString());
        if (operator == null) {
            condition.setOper(operatorEnum.toString());
            condition.setValue(value);
            return;
        }
        operator.resolveCondition(this.condition, operatorEnum.toString(), value);
    }

    @Override
    public Query equal(Object value) {
        resolveCondition(OperatorEnum.Equal, value);
        return query;
    }

    @Override
    public Query notEqual(Object value) {
        resolveCondition(OperatorEnum.NotEqual, value);
        return query;
    }

    @Override
    public Query contains(String value) {
        resolveCondition(OperatorEnum.Contains, value);
        return query;
    }

}
