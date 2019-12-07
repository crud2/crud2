package org.crud2.core.edit.impl.inner;

import org.crud2.core.edit.impl.AbstractDeleteImpl;
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

@Component
@Scope("prototype")
public class InnerDeleteImpl extends AbstractDeleteImpl {
    private static final Logger logger = LoggerFactory.getLogger(InnerDeleteImpl.class);
    @Autowired
    private SQLContextFactory sqlContextFactory;

    @Override
    public void flush() {
        PreparedSQLCommand command = buildDeleteCommand();
        sqlContextFactory.getSQLContext(dataSource).execute(command);
    }

    private PreparedSQLCommand buildDeleteCommand() {
        PreparedSQLCommandBuilder builder = PreparedSQLCommandBuilder.newInstance();
        if(!StringUtil.isNullOrEmpty(parameter.getSql())){
            builder.append(parameter.getSql());
        }
        else if(!StringUtil.isNullOrEmpty(parameter.getTable())) {
            builder.append("DELETE FROM %s", parameter.getTable());
            builder.append(" WHERE %s = ?", parameter.getKey());
            builder.appendParam(parameter.getKey(), parameter.getKeyValue());
        }else{
            logger.error("sql and edit table empty error");
        }
        return builder.build();
    }
}
