package org.crud2.core.edit.impl.inner;

import org.crud2.core.edit.impl.AbstractExecuteImpl;
import org.crud2.jdbc.PreparedSQLCommand;
import org.crud2.jdbc.PreparedSQLCommandBuilder;
import org.crud2.jdbc.SQLContextFactory;
import org.crud2.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class InnerExecuteImpl extends AbstractExecuteImpl {
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
        builder.append(parameter.getSql());
        return builder.build();
    }
}
