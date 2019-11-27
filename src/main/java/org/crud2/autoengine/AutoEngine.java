package org.crud2.autoengine;

import org.crud2.CRUD2BeanFactory;
import org.crud2.autoengine.config.Column;
import org.crud2.autoengine.config.Module;
import org.crud2.autoengine.config.ModuleDefineFactory;
import org.crud2.autoengine.exception.NullModuleException;
import org.crud2.autoengine.listsource.KeyValueListSourceParse;
import org.crud2.util.KeyValuePair;
import org.crud2.autoengine.listsource.ListSourceParse;
import org.crud2.autoengine.listsource.SqlListSourceParse;
import org.crud2.autoengine.sql.SqlTextParameterResolver;
import org.crud2.edit.Delete;
import org.crud2.edit.Insert;
import org.crud2.edit.Update;
import org.crud2.query.Query;
import org.crud2.query.result.PagerResult;
import org.crud2.util.Convert;
import org.crud2.util.RepeatableLinkedMap;
import org.crud2.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
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
        if (!StringUtil.isNullOrEmpty(module.getSql())) {
            String sql = module.getSql();
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

    public static PagerResult<List<Map<String, Object>>> queryListMapPager(
            String moduleId, Map<String, Object> params,
            int pageSize, int pageIndex,
            BeforeQueryHander beforeQueryHander, AfterQueryHandler<PagerResult<List<Map<String, Object>>>> afterQueryHandler) {
        Query query = query(moduleId, params);
        query.pageSizeIndex(pageSize, pageIndex);
        beforeQueryHander.handle(query);
        PagerResult<List<Map<String, Object>>> result = query.queryListMapPager();
        afterQueryHandler.handle(result);
        return result;
    }

    /**
     * query module datasource use module define
     *
     * @param moduleId
     * @param params            parameter for sql ,not for user defined where
     * @param pageIndex         page index,start from 1
     * @param pageSize
     * @param beforeQueryHander
     * @return
     */
    public static PagerResult<List<Map<String, Object>>> queryListMapPager(
            String moduleId, Map<String, Object> params,
            int pageSize, int pageIndex,
            BeforeQueryHander beforeQueryHander) {
        return queryListMapPager(moduleId, params, pageSize, pageIndex, beforeQueryHander, result -> {
            Module module = getAndCheckModule(moduleId);
            Map<String, RepeatableLinkedMap<String, Object>> listSources = new HashMap<>();
            for (Column column : module.getColumns()) {
                if (column.getListReplace() == 1) {
                    listSources.put(column.getName(), getSourceList(column));
                }
            }
            List<Map<String, Object>> data = result.getData();
            data.forEach(d -> {
                listSources.keySet().forEach(k -> {
                    String repText = listSources.get(k).getByValue(d.get(k)).getKey();
                    d.put(k + "_Rep", repText);
                });
            });
        });
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
                    insert.value(c.getName(), Convert.toObject(values.get(c.getName()), c.getSortType()));
                }
            }
        }
        if (keyColumn != null && keyColumn.getDefaultValueType() == 2) {
            insert.identity(keyColumn.getName());
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
        update.byKey(keyColumn.getName(), Convert.toObject(keyValObj, keyColumn.getValue()));
        Column[] columns = module.getColumns();
        Map<String, Object> updateValues = new HashMap<>();
        for (Column c : columns) {
            if (values.containsKey(c.getName())) {
                if (c != keyColumn || keyColumn.getDefaultValueType() != 2) {
                    update.set(c.getName(), Convert.toObject(values.get(c.getName()), c.getSortType()));
                }
            }
        }
        update.flush();
    }

    public static void delete(String moduleId, Object keyValue) {
        if (keyValue == null) {
            logger.error("key field must have value");
            return;
        }
        Module module = getAndCheckModule(moduleId);
        if (module == null) return;
        Delete delete = crud2BeanFactory.getDelete()
                .from(module.getEditTable());
        Column keyColumn = module.getKey();
        if (keyColumn == null) {
            logger.error(String.format("module:%s has no primary key defined", moduleId));
            return;
        }
        delete.byKey(keyColumn.getName(), Convert.toObject(keyValue, keyColumn.getSortType()));
        delete.flush();
    }

    public static void delete(String moduleId, Map<String, Object> values) {
        Module module = getAndCheckModule(moduleId);
        if (module == null) return;
        Delete delete = crud2BeanFactory.getDelete()
                .from(module.getEditTable());
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
        delete.byKey(keyColumn.getName(), Convert.toObject(keyValObj, keyColumn.getSortType()));
        delete.flush();
    }

    /**
     * get module column list source
     *
     * @param moduleId
     * @param columnName defined column name
     * @return
     */
    public static RepeatableLinkedMap<String, Object> getSourceList(String moduleId, String columnName) {
        Module module = getAndCheckModule(moduleId);
        if (module == null) return null;
        Column column = module.getColumn(columnName);
        if (column == null) return null;
        return getSourceList(column);
    }

    public static RepeatableLinkedMap<String, Object> getSourceList(Column column) {
        if (StringUtil.isNullOrEmpty(column.getValue())) {
            ListSourceParse listSourceParse = crud2BeanFactory.getBean(KeyValueListSourceParse.class);
            return listSourceParse.parse(column.getValue(), column.getSortType());
        } else if (StringUtil.isNullOrEmpty(column.getEditSql())) {
            ListSourceParse listSourceParse = crud2BeanFactory.getBean(SqlListSourceParse.class);
            return listSourceParse.parse(column.getEditSql(), column.getSortType());
        } else {
            logger.debug(String.format("column %s's list source didn't defined", column.getName()));
            return null;
        }
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

}
