package org.crud2.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
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
public class SQLContext {
    private static final Logger logger = LoggerFactory.getLogger(SQLContext.class);

    @Autowired
    JdbcTemplate template;

    public void execute(PreparedSQLCommand sqlCommand) {
        sqlCommand.debug(logger);
        int rows = template.update(sqlCommand.getCommandText(), sqlCommand.getParams());
        debug(rows);
    }

    public Map<String, Object> executeGeneratedKey(PreparedSQLCommand sqlCommand) {
        sqlCommand.debug(logger);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rows = template.update(con -> {
            PreparedStatement ps = con.prepareStatement(sqlCommand.getCommandText(), Statement.RETURN_GENERATED_KEYS);
            newArgPreparedStatementSetter(sqlCommand.getParams()).setValues(ps);
            return ps;
        }, keyHolder);
        debug(rows);
        Map<String, Object> keys = null;
        try {
            keys = keyHolder.getKeys();
        } catch (InvalidDataAccessApiUsageException ignored) {
        }
        return keys;
    }

    private PreparedStatementSetter newArgPreparedStatementSetter(Object[] args) {
        return new ArgumentPreparedStatementSetter(args);
    }

    private void debug(int rows) {
        if (logger.isDebugEnabled()) {
            logger.debug("Affected rows :" + rows);
        }
    }
}
