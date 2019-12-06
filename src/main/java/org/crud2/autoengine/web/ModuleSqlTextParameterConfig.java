package org.crud2.autoengine.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
public class ModuleSqlTextParameterConfig {
    Logger logger = LoggerFactory.getLogger(ModuleSqlTextParameterConfig.class);
    private Map<String, String[]> moduleSqlParameterMap;

    @Autowired
    private ModuleSqlTextParameterGetter moduleSqlTextParameterGetter;

    public ModuleSqlTextParameterConfig() {
        moduleSqlParameterMap = new HashMap<>();
    }

    public String[] get(String moduleId) {
        if (!moduleSqlParameterMap.containsKey(moduleId)) {
            String[] names = moduleSqlTextParameterGetter.get(moduleId);
            logger.debug(String.format("init module %s sql parameter names %s", moduleId, names == null ? "null" : Arrays.toString(names)));
            moduleSqlParameterMap.put(moduleId, names);
        }
        return moduleSqlParameterMap.get(moduleId);
    }

    public String[] get(String moduleId,String column){
        String mapKey = moduleId+"_"+column;
        if (!moduleSqlParameterMap.containsKey(mapKey)) {
            String[] names = moduleSqlTextParameterGetter.get(moduleId,column);
            logger.debug(String.format("init module %s sql parameter names %s", moduleId, names == null ? "null" : Arrays.toString(names)));
            moduleSqlParameterMap.put(moduleId, names);
        }
        return moduleSqlParameterMap.get(mapKey);
    }
}
