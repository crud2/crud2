package org.crud2.core.edit.impl.inner;

import org.crud2.core.edit.impl.AbstractInsertImpl;
import org.crud2.jdbc.PreparedSQLCommand;
import org.crud2.jdbc.PreparedSQLCommandBuilder;
import org.crud2.jdbc.SQLContext;
import org.crud2.jdbc.SQLContextFactory;
import org.crud2.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope("prototype")
public class InnerInsertImpl extends AbstractInsertImpl {
    private static final Logger logger = LoggerFactory.getLogger(InnerInsertImpl.class);
    @Autowired
    private SQLContextFactory sqlContextFactory;

    @Override
    public Object flush() {
        if (parameter.useSelectKey()) {
            PreparedSQLCommand selectKeyCommand = buildSelectKeyCommand();
            Object keyValue = sqlContextFactory.getSQLContext(dataSource).queryForObject(selectKeyCommand);
            parameter.setKeyValue(keyValue);
            parameter.getValues().remove(parameter.getKey());
            parameter.getValues().put(parameter.getKey(),keyValue);
        }
        PreparedSQLCommand sqlCommand = buildInsertCommand();
        if (parameter.useGenerateKey()) {
            Map<String, Object> keys = sqlContextFactory.getSQLContext(dataSource).executeGeneratedKey(sqlCommand);
            return keys.get(parameter.getKey());
        } else {
            sqlContextFactory.getSQLContext(dataSource).update(sqlCommand);
        }
        return null;
    }

    private PreparedSQLCommand buildSelectKeyCommand() {
        PreparedSQLCommandBuilder builder = PreparedSQLCommandBuilder.newInstance();
        builder.append(parameter.getSelectKeySql());
        return builder.build();
    }

    private PreparedSQLCommand buildInsertCommand() {
        PreparedSQLCommandBuilder builder = PreparedSQLCommandBuilder.newInstance();
        if (!StringUtil.isNullOrEmpty(parameter.getSql())) {
            builder.append(parameter.getSql());
        } else if (!StringUtil.isNullOrEmpty(parameter.getTable())) {
            builder.append("INSERT INTO %s (", parameter.getTable());
            String[] keys = new String[parameter.getValues().size()];
            parameter.getValues().keySet().toArray(keys);
            builder.append(keys);
            builder.append(") VALUES (");
            builder.appendPlaceolder(keys.length);
            builder.appendParam(keys, parameter.getValues());
            builder.append(")");
        } else {
            logger.error("sql and edit table empty error");
        }
        return builder.build();
    }
}
