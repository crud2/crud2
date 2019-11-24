package org.crud2.query.condition.operator;

import org.crud2.query.condition.Condition;
import org.crud2.query.condition.OperatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class NormalOperatorRegistrar {

    @Autowired
    private OperatorFactory operatorFactory;

    private static Logger logger = LoggerFactory.getLogger(NormalOperatorRegistrar.class);

    @PostConstruct
    private void reg() {
        logger.debug("start reg normal operator");
        //equal
        Operator searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper("=");
                condition.setValue(value);
            }

            @Override
            public String getName() {
                return "eq";
            }
        };
        operatorFactory.reg(searchOperator);
        //not equal
        searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper("<>");
                condition.setValue(value);
            }

            @Override
            public String getName() {
                return "ne";
            }
        };
        operatorFactory.reg(searchOperator);
        //less than
        searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper("<");
                condition.setValue(value);
            }

            @Override
            public String getName() {
                return "lt";
            }
        };
        operatorFactory.reg(searchOperator);
        //less than or equal to
        searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper("<=");
                condition.setValue(value);
            }

            @Override
            public String getName() {
                return "le";
            }
        };
        operatorFactory.reg(searchOperator);
        //greater than
        searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper(">");
                condition.setValue(value);
            }

            @Override
            public String getName() {
                return "gt";
            }
        };
        operatorFactory.reg(searchOperator);
        //greater than or equal to
        searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper(">=");
                condition.setValue(value);
            }

            @Override
            public String getName() {
                return "ge";
            }
        };
        operatorFactory.reg(searchOperator);
        //begin with
        searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper("like");
                condition.setValue(value + "%");
            }

            @Override
            public String getName() {
                return "bw";
            }
        };
        operatorFactory.reg(searchOperator);
        //begin not with
        searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper("not like");
                condition.setValue(value + "%");
            }

            @Override
            public String getName() {
                return "bn";
            }
        };
        operatorFactory.reg(searchOperator);
        //include
        searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper("in");
                condition.setValue(value.toString().split(","));
            }

            @Override
            public String getName() {
                return "in";
            }
        };
        operatorFactory.reg(searchOperator);
        //not include
        searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper("not in");
                condition.setValue(value.toString().split(","));
            }

            @Override
            public String getName() {
                return "ni";
            }
        };
        operatorFactory.reg(searchOperator);
        //end with
        searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper("like");
                condition.setValue("%" + value);
            }

            @Override
            public String getName() {
                return "ew";
            }
        };
        operatorFactory.reg(searchOperator);
        //end not with
        searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper("not like");
                condition.setValue("%" + value);
            }

            @Override
            public String getName() {
                return "en";
            }
        };
        operatorFactory.reg(searchOperator);
        //contains
        searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper("like");
                condition.setValue("%" + value + "%");
            }

            @Override
            public String getName() {
                return "cn";
            }
        };
        operatorFactory.reg(searchOperator);
        //contains
        searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper("not like");
                condition.setValue("%" + value + "%");
            }

            @Override
            public String getName() {
                return "nc";
            }
        };
        operatorFactory.reg(searchOperator);
        //is null
        searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper("is null");
            }

            @Override
            public String getName() {
                return "nu";
            }
        };
        operatorFactory.reg(searchOperator);
        //is not null
        searchOperator = new Operator() {
            @Override
            public void resolveCondition(Condition condition, String operName, Object value) {
                condition.setOper("is not null");
            }

            @Override
            public String getName() {
                return "nn";
            }
        };
        operatorFactory.reg(searchOperator);
    }
}
