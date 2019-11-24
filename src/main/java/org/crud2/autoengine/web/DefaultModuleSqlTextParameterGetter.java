package org.crud2.autoengine.web;

import org.crud2.autoengine.AutoEngine;
import org.springframework.stereotype.Component;

/***
 * default impl for ModuleSqlTextParameterGetter
 * if web & service is not in on scope,will throw exception
 */
@Component
public class DefaultModuleSqlTextParameterGetter implements ModuleSqlTextParameterGetter {
    @Override
    public String[] get(String moduleId) {
        return AutoEngine.getModuleQueryParameterNames(moduleId);
    }
}
