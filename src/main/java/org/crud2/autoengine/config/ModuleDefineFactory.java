package org.crud2.autoengine.config;

import org.crud2.autoengine.sql.SqlTextParameterResolver;
import org.crud2.util.ResourceUtil;
import org.crud2.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class ModuleDefineFactory {
    private static final Logger logger = LoggerFactory.getLogger(ModuleDefineFactory.class);

    @Autowired
    private ModuleConfigReader moduleConfigReader;
    @Autowired
    private SqlTextParameterResolver sqlTextParameterResolver;

    private Resource[] moduleConfigs;
    private Map<String, Module> moduleMap;
    private Map<String, String> originalDefineMap;
    private Map<String, String[]> sqlParameterNames;

    public ModuleDefineFactory() {
        moduleMap = new HashMap<>();
        sqlParameterNames = new HashMap<>();
        originalDefineMap = new HashMap<>();
    }

    public void setModuleConfigs(Resource[] moduleConfigs) {
        this.moduleConfigs = moduleConfigs;
    }

    public Module get(String id) {
        return moduleMap.getOrDefault(id, null);
    }

    public String[] getModuleSqlParameterNames(String moduleId) {
        if (!sqlParameterNames.containsKey(moduleId)) {
            Module module = get(moduleId);
            if (module == null) sqlParameterNames.put(moduleId, null);
            else {
                String sql = module.getSql();
                String[] names = sqlTextParameterResolver.getNames(sql);
                sqlParameterNames.put(moduleId, names);
            }
        }
        return sqlParameterNames.getOrDefault(moduleId, null);
    }

    public String[] getModuleSqlParameterNames(String moduleId, String columnName) {
        String mapkey = moduleId + "_" + columnName;
        if (!sqlParameterNames.containsKey(mapkey)) {
            Module module = get(moduleId);
            if (module == null) sqlParameterNames.put(mapkey, null);
            else {
                Column column = module.getColumn(columnName);
                if (column == null) sqlParameterNames.put(mapkey, null);
                else {
                    String sql = module.getSql();
                    String[] names = sqlTextParameterResolver.getNames(sql);
                    sqlParameterNames.put(mapkey, names);
                }
            }
        }
        return sqlParameterNames.getOrDefault(mapkey, null);
    }

    public void regModule(Module module) {
        if (moduleMap.containsKey(module.getId())) moduleMap.remove(module.getId());
        moduleMap.put(module.getId(), module);
    }

    public String getOrignalConfig(String moduleId) {
        return originalDefineMap.getOrDefault(moduleId, null);
    }

    public void regModules() {
        logger.info("reg modules");
        if (moduleConfigs == null || moduleConfigs.length == 0) {
            return;
        }

        for (org.springframework.core.io.Resource resource : moduleConfigs) {
            String configString;
            try {
                configString = ResourceUtil.readText(resource);
            } catch (IOException e) {
                logger.error(String.format("read module config file: %s fail", resource.getFilename()), e);
                continue;
            }
            if (configString.length() == 0) {
                logger.error(String.format("read module config file: %s fail,empty string", resource.getFilename()));
                continue;
            }
            Module module = moduleConfigReader.read(configString);
            if (module == null) {
                logger.debug(String.format("read module config file: %s fail", resource.getFilename()));
                continue;
            }
            originalDefineMap.put(module.getId(), configString);
            logger.debug(String.format("module config file '%s' load complete", resource.getFilename()));
            regModule(module);
        }
        logger.info("reg modules complete");
    }
}
