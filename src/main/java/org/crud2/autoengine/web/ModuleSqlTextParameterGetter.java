package org.crud2.autoengine.web;

public interface ModuleSqlTextParameterGetter {
    String[] get(String moduleId);
    String[] get(String moduleId,String column);
}