package org.crud2.autoengine.exception;

public class NullModuleException extends Exception {
    public NullModuleException(String moduleId) {
        super(String.format("module config %s not find", moduleId));
    }
}
