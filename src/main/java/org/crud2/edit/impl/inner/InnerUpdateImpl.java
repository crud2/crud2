package org.crud2.edit.impl.inner;

import org.crud2.edit.impl.AbstractUpdateImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Map;

@Component
@Scope("prototype")
public class InnerUpdateImpl extends AbstractUpdateImpl {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void flush() {
        PreparedSQLCommand sqlCommand = buildUpdateCommand();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlCommand.getCommandText(), Statement.RETURN_GENERATED_KEYS);
            newArgPreparedStatementSetter(sqlCommand.getParamNames()).setValues(ps);
            return ps;
        });
    }

    private PreparedStatementSetter newArgPreparedStatementSetter(String[] argNames) {
        Object[] args = new Object[argNames.length];
        Map<String, Object> values = parameter.getValues();
        for (int i = 0; i < argNames.length; i++) {
            args[i] = values.get(argNames[i]);
        }

        return new ArgumentPreparedStatementSetter(args);
    }

    private PreparedSQLCommand buildUpdateCommand() {
        PreparedSQLCommandBuilder builder = PreparedSQLCommandBuilder.newInstance();
        builder.append("UPDATE %s  SET ", parameter.getTable());
        String[] keys = new String[parameter.getValues().size()];
        parameter.getValues().keySet().toArray(keys);
        builder.append(keys, k -> k + "=?");
        builder.append(" WHERE %s = ?", parameter.getKey());
        builder.appendParam(keys);
        builder.appendParam(parameter.getKey());
        return builder.build();
    }
}
