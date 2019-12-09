package org.crud2.autoengine;

import org.crud2.CRUD;
import org.crud2.CRUD2BeanFactory;
import org.crud2.autoengine.config.Column;
import org.crud2.autoengine.config.Module;
import org.crud2.autoengine.config.ModuleDefineFactory;
import org.crud2.autoengine.exception.NullModuleException;
import org.crud2.autoengine.listsource.KeyValueListSourceParse;
import org.crud2.autoengine.listsource.ListSourceParse;
import org.crud2.autoengine.listsource.SqlListSourceParse;
import org.crud2.autoengine.plugin.Plugin;
import org.crud2.autoengine.sql.SqlTextParameterResolver;
import org.crud2.core.edit.Delete;
import org.crud2.core.edit.Insert;
import org.crud2.core.edit.Update;
import org.crud2.core.query.Query;
import org.crud2.core.query.result.PagerResult;
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

    public static Module getModule(String moduleId) {
        return getAndCheckModule(moduleId);
    }

    // region query

    public static Query query(String moduleId) {
        return query(moduleId, null);
    }

    public static Query query(String moduleId, Map<String, Object> params) {
        Module module = getAndCheckModule(moduleId);
        if (module == null) return null;
        Query query = crud2BeanFactory.getQuery();
        query.setDataSource(crud2BeanFactory.getBean(AutoEngineSessionBean.class).getDataSource(moduleId));
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

    // endregion

    // region queryPluginList
    public static List queryPluginList(String pluginName, String moduleId, Map<String, Object> params) {
        Plugin plugin = (Plugin) crud2BeanFactory.getBean(pluginName);
        return queryPluginList(plugin, moduleId, params);
    }

    public static <T> List<T> queryPluginList(Plugin<T> plugIn, String moduleId, Map<String, Object> params) {
        Module module = getAndCheckModule(moduleId);
        //TODO:muti datasource
        return plugIn.query(module, params);
    }

    //endregion

    /**
     * query module datasource use module define
     *
     * @param moduleId
     * @param params             parameter for sql ,not for user defined where
     * @param beforeQueryHandler callback for before query execute , such as use for prepare where condition
     * @param afterQueryHandler  callback for after query execute, such as secondary processing of result set
     * @return
     */
    public static List<Map<String, Object>> queryListMap(
            String moduleId, Map<String, Object> params,
            BeforeQueryHander beforeQueryHandler, AfterQueryHandler<List<Map<String, Object>>> afterQueryHandler) {
        Query query = query(moduleId, params);
        query.setDataSource(crud2BeanFactory.getBean(AutoEngineSessionBean.class).getDataSource(moduleId));
        if (beforeQueryHandler != null) beforeQueryHandler.handle(query);
        List<Map<String, Object>> result = query.queryListMap();
        if (afterQueryHandler != null) afterQueryHandler.handle(result);
        return result;
    }

    /**
     * query module datasource use module define
     *
     * @param moduleId
     * @param params             parameter for sql ,not for user defined where
     * @param beforeQueryHandler callback for before query execute , such as use for prepare where condition
     * @return
     */
    public static List<Map<String, Object>> queryListMap(
            String moduleId, Map<String, Object> params,
            BeforeQueryHander beforeQueryHandler) {
        return queryListMap(moduleId, params, beforeQueryHandler, result -> {
            Module module = getAndCheckModule(moduleId);
            Map<String, RepeatableLinkedMap<String, Object>> listSources = new HashMap<>();
            for (Column column : module.getColumns()) {
                if (column.getListReplace() == 1) {
                    listSources.put(column.getName(), getSourceList(column, params));
                }
            }
            result.forEach(d -> {
                listSources.keySet().forEach(k -> {
                    String repText = listSources.get(k).getByValue(d.get(k)).getKey();
                    d.put(k + "_Rep", repText);
                });
            });
        });
    }

    /**
     * query module datasource use module define
     *
     * @param moduleId
     * @param params   parameter for sql ,not for user defined where
     * @return
     */
    public static List<Map<String, Object>> queryListMap(
            String moduleId, Map<String, Object> params) {
        return queryListMap(moduleId, params, null, null);
    }

    /**
     * execute module sql script
     *
     * @param moduleId
     * @param params   parameter for sql ,not for user defined where
     * @return
     */
    public static void execute(
            String moduleId, Map<String, Object> params) {
        Module module = getAndCheckModule(moduleId);
        if (module == null) return;
        String sql = crud2BeanFactory.getBean(SqlTextParameterResolver.class).resolve(module.getSql(), params);
        crud2BeanFactory.getExecute().setDataSource(crud2BeanFactory.getBean(AutoEngineSessionBean.class).getDataSource(moduleId)).sql(sql).flush();
    }

    /**
     * query module pager datasource use module define
     *
     * @param moduleId
     * @param params             parameter for sql ,not for user defined where
     * @param pageSize           page size ,such as 20
     * @param pageIndex          page index,start from 1
     * @param beforeQueryHandler callback for before query execute , such as use for prepare where condition
     * @param afterQueryHandler  callback for after query execute, such as secondary processing of result set
     * @return
     */
    public static PagerResult<List<Map<String, Object>>> queryListMap(
            String moduleId, Map<String, Object> params,
            int pageSize, int pageIndex,
            BeforeQueryHander beforeQueryHandler, AfterQueryHandler<PagerResult<List<Map<String, Object>>>> afterQueryHandler) {
        Query query = query(moduleId, params);
        query.setDataSource(crud2BeanFactory.getBean(AutoEngineSessionBean.class).getDataSource(moduleId));
        query.pageSizeIndex(pageSize, pageIndex);
        beforeQueryHandler.handle(query);
        PagerResult<List<Map<String, Object>>> result = query.queryListMapPager();
        afterQueryHandler.handle(result);
        return result;
    }

    /**
     * query module pager datasource use module define
     *
     * @param moduleId
     * @param params             parameter for sql ,not for user defined where
     * @param pageIndex          page index,start from 1
     * @param pageSize           page size ,such as 20
     * @param beforeQueryHandler callback for before query execute , such as use for prepare where condition
     * @return
     */
    public static PagerResult<List<Map<String, Object>>> queryListMap(
            String moduleId, Map<String, Object> params,
            int pageSize, int pageIndex,
            BeforeQueryHander beforeQueryHandler) {
        return queryListMap(moduleId, params, pageSize, pageIndex, beforeQueryHandler, result -> {
            Module module = getAndCheckModule(moduleId);
            Map<String, RepeatableLinkedMap<String, Object>> listSources = new HashMap<>();
            for (Column column : module.getColumns()) {
                if (column.getListReplace() == 1) {
                    listSources.put(column.getName(), getSourceList(column, params));
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

    public static Map<String, Object> getMap(String moduleId, Map<String, Object> params) {
        Query query = query(moduleId, params);
        query.setDataSource(crud2BeanFactory.getBean(AutoEngineSessionBean.class).getDataSource(moduleId));
        return query.getMap();
    }

    public static String[] getModuleQueryParameterNames(String moduleId) {
        return moduleDefineFactory.getModuleSqlParameterNames(moduleId);
    }

    public static String[] getModuleColumnListSourceParameterNames(String moduleId, String column) {
        return moduleDefineFactory.getModuleSqlParameterNames(moduleId, column);
    }

    public static Object insert(String moduleId, Map<String, Object> values) {
        Module module = getAndCheckModule(moduleId);
        if (module == null) return null;
        Insert insert = crud2BeanFactory.getInsert()
                .into(module.getEditTable());
        insert.setDataSource(crud2BeanFactory.getBean(AutoEngineSessionBean.class).getDataSource(moduleId));
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
        update.setDataSource(crud2BeanFactory.getBean(AutoEngineSessionBean.class).getDataSource(moduleId));
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
        update.byKey(keyColumn.getName(), Convert.toObject(keyValObj, keyColumn.getSortType()));
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
        delete.setDataSource(crud2BeanFactory.getBean(AutoEngineSessionBean.class).getDataSource(moduleId));
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
        delete.setDataSource(crud2BeanFactory.getBean(AutoEngineSessionBean.class).getDataSource(moduleId));
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
        return getSourceList(moduleId, columnName, null);
    }

    /**
     * get module column list source
     *
     * @param moduleId
     * @param columnName defined column name
     * @param parameters sql parameters
     * @return
     */
    public static RepeatableLinkedMap<String, Object> getSourceList(String moduleId, String columnName, Map<String, Object> parameters) {
        Module module = getAndCheckModule(moduleId);
        if (module == null) return null;
        Column column = module.getColumn(columnName);
        if (column == null) return null;
        return getSourceList(column, parameters);
    }

    public static RepeatableLinkedMap<String, Object> getSourceList(Column column, Map<String, Object> parameters) {
        if (!StringUtil.isNullOrEmpty(column.getValue())) {
            ListSourceParse listSourceParse = crud2BeanFactory.getBean(KeyValueListSourceParse.class);
            return listSourceParse.parse(column.getValue(), column.getSortType(), parameters);
        } else if (!StringUtil.isNullOrEmpty(column.getEditSql())) {
            ListSourceParse listSourceParse = crud2BeanFactory.getBean(SqlListSourceParse.class);
            return listSourceParse.parse(column.getEditSql(), column.getSortType(), parameters);
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
