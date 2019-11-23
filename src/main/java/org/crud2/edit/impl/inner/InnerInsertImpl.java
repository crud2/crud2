package org.crud2.edit.impl.inner;

import org.crud2.edit.EditParameter;
import org.crud2.edit.impl.AbstractInsertImpl;
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

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Object flush() {
        PreparedSQLCommand sqlCommand = buildInsertCommand();
        if (parameter.isIdentity()) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            int rows = jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sqlCommand.getCommandText(), Statement.RETURN_GENERATED_KEYS);
                newArgPreparedStatementSetter(sqlCommand.getParamNames()).setValues(ps);
                return ps;
            }, keyHolder);
            if (rows > 0) {
                Map<String, Object> keys = keyHolder.getKeys();
                return keys.get(parameter.getKey());
            } else {
                //todo:empty row effect
                return null;
            }
        } else {
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sqlCommand.getCommandText(), Statement.RETURN_GENERATED_KEYS);
                newArgPreparedStatementSetter(sqlCommand.getParamNames()).setValues(ps);
                return ps;
            });
        }
        return null;
    }

    private PreparedStatementSetter newArgPreparedStatementSetter(String[] argNames) {
        Object[] args = new Object[argNames.length];
        Map<String, Object> values = parameter.getValues();
        for (int i = 0; i < argNames.length; i++) {
            args[i] = values.get(argNames[i]);
        }
        return new ArgumentPreparedStatementSetter(args);
    }

    private PreparedSQLCommand buildInsertCommand() {
        PreparedSQLCommandBuilder builder = PreparedSQLCommandBuilder.newInstance();
        builder.append("INSERT INTO %s (", parameter.getTable());
        String[] keys = new String[parameter.getValues().size()];
        parameter.getValues().keySet().toArray(keys);
        builder.append(keys);
        builder.append(") VALUES (");
        builder.appendPlaceolder(keys.length);
        builder.appendParam(keys);
        builder.append(")");
        return builder.build();
    }
}
