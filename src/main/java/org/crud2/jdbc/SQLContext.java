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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SQLContext {
    private static final Logger logger = LoggerFactory.getLogger(SQLContext.class);

    @Autowired
    JdbcTemplate template;
    @Autowired
    ColumnKeyNameResolver columnKeyNameResolver;

    public void execute(PreparedSQLCommand command) {
        command.debug(logger);
        int rows = template.update(command.getCommandText(), command.getParams());
        debugAffect(rows);
    }

    public Map<String, Object> executeGeneratedKey(PreparedSQLCommand command) {
        command.debug(logger);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            int rows = template.update(con -> {
                PreparedStatement ps = con.prepareStatement(command.getCommandText(), Statement.RETURN_GENERATED_KEYS);
                newArgPreparedStatementSetter(command.getParams()).setValues(ps);
                return ps;
            }, keyHolder);
            debugAffect(rows);
        } catch (Exception ex) {
            logger.error("execute generatedKey error", ex);
            throw ex;
        }
        Map<String, Object> keys = null;
        try {
            keys = keyHolder.getKeys();
        } catch (InvalidDataAccessApiUsageException ignored) {
        }
        return keys;
    }

    public DataTable queryDataTable(PreparedSQLCommand command) {
        DataTableRowMapper rowMapper = new DataTableRowMapper();
        List<DataRow> rowList = template.query(command.getCommandText(), command.getParams(), rowMapper);
        debugTotal(rowList.size());
        DataTable table = new DataTable();
        String[] columns = new String[rowMapper.getColumnCount()];
        for (int i = 0; i < columns.length; i++) {
            assert rowMapper.getColumnNames() != null;
            columns[i] = columnKeyNameResolver.resolve(rowMapper.getColumnNames()[i]);
        }
        table.setColumns(columns);
        table.setColumnTypes(rowMapper.getColumnTypes());
        DataRow[] rows = new DataRow[rowList.size()];
        table.setRows(rows);
        return table;
    }

    /***
     * execute query and return a list map result
     * by default,use ColumnKeyNameResolver from resolver factory
     * @param command
     * @return LinkedHashMap result
     */
    public List<Map<String, Object>> queryForMapList(PreparedSQLCommand command) {
        command.debug(logger);
        List<Map<String, Object>> result = template.query(command.getCommandText(), command.getParams(), getColumnRowMapper());
        debugTotal(result.size());
        return result;
    }

    /***
     * execute query and return a map result
     * @param command
     * @param columnKeyNameResolver user defined column key-name resolver
     * @return LinkedHashMap result
     */
    public List<Map<String, Object>> queryForMapList(PreparedSQLCommand command, ColumnKeyNameResolver columnKeyNameResolver) {
        command.debug(logger);
        List<Map<String, Object>> result = template.query(command.getCommandText(), command.getParams(), getColumnRowMapper(columnKeyNameResolver));
        debugTotal(result.size());
        return result;
    }

    /***
     * execute query and return a long value
     * may be use for query count
     * @param command
     * @return
     */
    public long queryForLong(PreparedSQLCommand command) {
        command.debug(logger);
        try {
            Long result = template.queryForObject(command.getCommandText(), command.getParams(), Long.class);
            debugTotal(1);
            return result;
        } catch (NullPointerException ex) {
            logger.error("query for long result maybe null", ex);
            throw ex;
        }
    }

    /***
     * execute query and return a map (single row result)
     * by default,use ColumnKeyNameResolver from resolver factory
     * @param command
     * @return LinkedHashMap result
     */
    public Map<String, Object> queryForMap(PreparedSQLCommand command) {
        List<Map<String, Object>> resultList = queryForMapList(command);
        if (resultList.size() == 0) return null;
        return resultList.get(0);
        /*
        Map<String, Object> result = template.queryForObject(command.getCommandText(), command.getParams(), getColumnRowMapper());
        debugTotal(result == null ? 0 : 1);
        return result;
        */
    }

    private void query(PreparedSQLCommand command, RowCallbackHandler rowCallbackHandler) {
        command.debug(logger);
        template.query(command.getCommandText(), command.getParams(), rowCallbackHandler);
    }

    private PreparedStatementSetter newArgPreparedStatementSetter(Object[] args) {
        return new ArgumentPreparedStatementSetter(args);
    }

    private RowMapper<Map<String, Object>> getColumnRowMapper() {
        return new ExColumnMapRowMapper();
    }

    private RowMapper<Map<String, Object>> getColumnRowMapper(ColumnKeyNameResolver columnKeyNameResolver) {
        return new ExColumnMapRowMapper(columnKeyNameResolver);
    }

    // region log debug info

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

    // endregion
}
