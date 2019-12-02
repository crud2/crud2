package org.crud2.edit.impl.inner;

import org.crud2.edit.impl.AbstractInsertImpl;
import org.crud2.jdbc.PreparedSQLCommand;
import org.crud2.jdbc.PreparedSQLCommandBuilder;
import org.crud2.jdbc.SQLContext;
import org.crud2.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;

@Component
@Scope("prototype")
public class InnerInsertImpl extends AbstractInsertImpl {
    private static final Logger logger = LoggerFactory.getLogger(InnerInsertImpl.class);

    @Autowired
    SQLContext context;

    @Override
    public Object flush() {
        PreparedSQLCommand sqlCommand = buildInsertCommand();
        if (parameter.isIdentity()) {
            Map<String, Object> keys = context.executeGeneratedKey(sqlCommand);
            return keys.get(parameter.getKey());
        } else {
            context.execute(sqlCommand);
        }
        return null;
    }

    private PreparedSQLCommand buildInsertCommand() {
        PreparedSQLCommandBuilder builder = PreparedSQLCommandBuilder.newInstance();
        if(!StringUtil.isNullOrEmpty(parameter.getSql())){
            builder.append(parameter.getSql());
        }
        else if(!StringUtil.isNullOrEmpty(parameter.getTable())) {
            builder.append("INSERT INTO %s (", parameter.getTable());
            String[] keys = new String[parameter.getValues().size()];
            parameter.getValues().keySet().toArray(keys);
            builder.append(keys);
            builder.append(") VALUES (");
            builder.appendPlaceolder(keys.length);
            builder.appendParam(keys, parameter.getValues());
            builder.append(")");
        }else{
            logger.error("sql and edit table empty error");
        }
        return builder.build();
    }
}
