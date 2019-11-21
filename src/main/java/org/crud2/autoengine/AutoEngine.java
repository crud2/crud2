package org.crud2.autoengine;

import org.crud2.CRUD2BeanFactory;
import org.crud2.autoengine.config.Column;
import org.crud2.autoengine.config.Module;
import org.crud2.autoengine.config.ModuleDefineFactory;
import org.crud2.autoengine.exception.NullModuleException;
import org.crud2.autoengine.sql.SqlTextParameterResolver;
import org.crud2.edit.Delete;
import org.crud2.edit.Insert;
import org.crud2.edit.Update;
import org.crud2.query.Query;
import org.crud2.util.Convert;
import org.crud2.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AutoEngine {
    private static Logger logger = LoggerFactory.getLogger(AutoEngine.class);

    private static CRUD2BeanFactory crud2BeanFactory;
    private static ModuleDefineFactory moduleDefineFactory;
    private static SqlTextParameterResolver sqlTextParameterResolver;

    public void init(
            CRUD2BeanFactory crud2BeanFactory,
            ModuleDefineFactory moduleDefineFactory,
            SqlTextParameterResolver sqlTextParameterResolver) {
        AutoEngine.crud2BeanFactory = crud2BeanFactory;
        AutoEngine.moduleDefineFactory = moduleDefineFactory;
        AutoEngine.sqlTextParameterResolver = sqlTextParameterResolver;
    }

    public static Query query(String moduleId) {
        return query(moduleId, null);
    }

    public static Query query(String moduleId, Map<String, Object> params) {
        Module module = getAndCheckModule(moduleId);
        if (module == null) return null;
        Query query = crud2BeanFactory.getQuery();
        if (!StringUtil.isNullOrEmpty(module.getQuerySql())) {
            String sql = module.getQuerySql();
            if (params != null) sql = sqlTextParameterResolver.resolve(sql, params);
            query.sql(sql);
        } else {
            if (!StringUtil.isNullOrEmpty(module.getEditTable())) {
                query.from(module.getEditTable());
            } else {
                query.from(moduleId);
            }
            if (module.getColumns() != null && module.getColumns().length > 0) {
                String[] selectColumns = new String[module.getColumns().length];
                for (int i = 0; i < module.getColumns().length; i++) {
                    selectColumns[i] = module.getColumns()[i].getName();
                }
                query.select(selectColumns);
            } else {
                query.select("*");
            }
        }
        return query;
    }

    public static String[] getModuleQueryParameterNames(String moduleId) {
        return moduleDefineFactory.getModuleSqlParameterNames(moduleId);
    }

    public static Object insert(String moduleId, Map<String, Object> values) {
        Module module = getAndCheckModule(moduleId);
        if (module == null) return null;
        Insert insert = crud2BeanFactory.getInsert()
                .into(module.getEditTable());
        Column keyColumn = module.getKey();
        if (keyColumn == null) {
            logger.debug(String.format("module:%s has no primary key defined", moduleId));
        }
        Column[] columns = module.getColumns();
        Map<String, Object> insertValues = new HashMap<>();
        for (Column c : columns) {
            if (values.containsKey(c.getName())) {
                if (c != keyColumn || keyColumn.getDefaultValueType() != 2) {
                    insert.value(c.getName(), convertEditValue(c, values.get(c.getName())));
                }
            }
        }
        if (keyColumn != null && keyColumn.getDefaultValueType() == 2) {
            insert.identity();
        }
        return insert.flush();
    }

    public static void update(String moduleId, Map<String, Object> values) {
        Module module = getAndCheckModule(moduleId);
        if (module == null) return;
        Update update = crud2BeanFactory.getUpdate()
                .table(module.getEditTable());
        Column keyColumn = module.getKey();
        if (keyColumn == null) {
            logger.error(String.format("module:%s has no primary key defined", moduleId));
            return;
        }
        Object keyValObj = values.getOrDefault(keyColumn.getName(), null);
        if (keyValObj == null) {
            logger.error(String.format("key %s must have value", keyColumn.getName()));
            return;
        }
        update.byKey(keyColumn.getName(), convertEditValue(keyColumn, keyValObj));
        Column[] columns = module.getColumns();
        Map<String, Object> updateValues = new HashMap<>();
        for (Column c : columns) {
            if (values.containsKey(c.getName())) {
                if (c != keyColumn || keyColumn.getDefaultValueType() != 2) {
                    update.set(c.getName(), convertEditValue(c, values.get(c.getName())));
                }
            }
        }
        update.flush();
    }

    public static Delete delete(String moduleId) {
        return null;
    }

    private static Module getAndCheckModule(String moduleId) {
        Module module = moduleDefineFactory.get(moduleId);
        if (module == null) {
            NullModuleException exception = new NullModuleException(moduleId);
            logger.error(exception.getMessage(), exception);
            return null;
        }
        return module;
    }

    private static Object convertEditValue(Column column, Object value) {
        switch (column.getSortType()) {
            case "float":
                return Convert.toDecimal(value);
            case "date":
                return Convert.toDate(value);
            case "int":
                return Convert.toInt(value);
            case "text":
                return value.toString();
        }
        return value;
    }
}
