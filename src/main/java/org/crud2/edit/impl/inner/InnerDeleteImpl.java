package org.crud2.edit.impl.inner;

import org.crud2.edit.impl.AbstractDeleteImpl;
import org.crud2.jdbc.PreparedSQLCommand;
import org.crud2.jdbc.PreparedSQLCommandBuilder;
import org.crud2.jdbc.SQLContext;
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
    SQLContext context;

    @Override
    public void flush() {
        PreparedSQLCommand command = buildDeleteCommand();
        context.execute(command);
    }

    private PreparedSQLCommand buildDeleteCommand() {
        PreparedSQLCommandBuilder builder = PreparedSQLCommandBuilder.newInstance();
        builder.append("DELETE FROM %s", parameter.getTable());
        builder.append(" WHERE %s = ?", parameter.getKey());
        builder.appendParam(parameter.getKey(), parameter.getKeyValue());
        return builder.build();
    }
}
