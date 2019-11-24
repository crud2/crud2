package org.crud2.query.condition;

import org.crud2.query.condition.operator.Operator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class OperatorFactory {
    private Map<String, Operator> operatorMap= new HashMap<>();

    private static Logger logger = LoggerFactory.getLogger(OperatorFactory.class);

    public void reg(Operator searchOperator) {
        operatorMap.remove(searchOperator.getName());
        operatorMap.put(searchOperator.getName(), searchOperator);
        logger.debug(String.format("operator %s registered",searchOperator.getName()));
    }

    public Operator getOperator(String name) {
        if (!operatorMap.containsKey(name)) return null;
        return operatorMap.get(name);
    }
}
