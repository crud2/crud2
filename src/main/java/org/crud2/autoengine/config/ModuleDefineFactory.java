package org.crud2.autoengine.config;

import org.crud2.autoengine.sql.SqlTextParameterResolver;
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
    private Map<String, String[]> sqlParameterNames;

    public ModuleDefineFactory() {
        moduleMap = new HashMap<>();
        sqlParameterNames = new HashMap<>();
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

    public void regModules() {
        logger.info("reg modules");
        if (moduleConfigs == null || moduleConfigs.length == 0) {
            return;
        }

        for (org.springframework.core.io.Resource resource : moduleConfigs) {
            InputStream stream = null;
            try {
                stream = resource.getInputStream();
            } catch (IOException e) {
                logger.error(String.format("read module config file: %s fail", resource.getFilename()), e);
                continue;
            }
            Module module = moduleConfigReader.read(stream);
            if (module == null) {
                logger.debug(String.format("read module config file: %s fail", resource.getFilename()));
                continue;
            }
            logger.debug(String.format("module config file '%s' load complete", resource.getFilename()));
            moduleMap.put(module.getId(), module);
        }
        logger.info("reg modules complete");
    }
}
