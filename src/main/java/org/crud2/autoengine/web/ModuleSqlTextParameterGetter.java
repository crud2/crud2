package org.crud2.autoengine.web;

@FunctionalInterface
public interface ModuleSqlTextParameterGetter {
    String[] get(String moduleId);
}