package org.crud2.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@Component
public class SQLContext {
    private static final Logger logger = LoggerFactory.getLogger(SQLContext.class);

    @Autowired
    JdbcTemplate template;

    public void execute(PreparedSQLCommand command) {
        command.debug(logger);
        int rows = template.update(command.getCommandText(), command.getParams());
        debugAffect(rows);
    }

    public Map<String, Object> executeGeneratedKey(PreparedSQLCommand command) {
        command.debug(logger);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rows = template.update(con -> {
            PreparedStatement ps = con.prepareStatement(command.getCommandText(), Statement.RETURN_GENERATED_KEYS);
            newArgPreparedStatementSetter(command.getParams()).setValues(ps);
            return ps;
        }, keyHolder);
        debugAffect(rows);
        Map<String, Object> keys = null;
        try {
            keys = keyHolder.getKeys();
        } catch (InvalidDataAccessApiUsageException ignored) {
        }
        return keys;
    }

    public void query(PreparedSQLCommand command) {
        command.debug(logger);
        RowCallbackHandler rowCallbackHandler = new RowCountCallbackHandler() {
            @Override
            protected void processRow(ResultSet rs, int rowNum) throws SQLException {
            }
        };
        template.query(command.getCommandText(), command.getParams(), rowCallbackHandler);
    }

    private PreparedStatementSetter newArgPreparedStatementSetter(Object[] args) {
        return new ArgumentPreparedStatementSetter(args);
    }

    private void debugAffect(int rows) {
        if (logger.isDebugEnabled()) {
            logger.debug("<==   Affected: " + rows);
        }
    }

    private void debugTotal(int rows) {
        if (logger.isDebugEnabled()) {
            logger.debug("<==      Total: " + rows);
        }
    }
}
