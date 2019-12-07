package org.crud2.core.edit.impl.inner;

import org.crud2.core.edit.impl.AbstractUpdateImpl;
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
public class InnerUpdateImpl extends AbstractUpdateImpl {
    private static final Logger logger = LoggerFactory.getLogger(InnerUpdateImpl.class);

    @Autowired
    private SQLContextFactory sqlContextFactory;

    @Override
    public void flush() {
        PreparedSQLCommand sqlCommand = buildUpdateCommand();
        sqlContextFactory.getSQLContext(dataSource).execute(sqlCommand);
    }

    private PreparedSQLCommand buildUpdateCommand() {
        PreparedSQLCommandBuilder builder = PreparedSQLCommandBuilder.newInstance();
        if(!StringUtil.isNullOrEmpty(parameter.getSql())){
            builder.append(parameter.getSql());
        }
        else if(!StringUtil.isNullOrEmpty(parameter.getTable())) {
            builder.append("UPDATE %s  SET ", parameter.getTable());
            String[] keys = new String[parameter.getValues().size()];
            parameter.getValues().keySet().toArray(keys);
            builder.append(keys, k -> k + "=?");
            builder.append(" WHERE %s = ?", parameter.getKey());
            builder.appendParam(keys, parameter.getValues());
            builder.appendParam(parameter.getKey(), parameter.getKeyValue());
        }else{
            logger.error("sql and edit table empty error");
        }
        return builder.build();
    }
}
