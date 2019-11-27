package org.crud2.autoengine;

@FunctionalInterface
public interface AfterQueryHandler<TResult> {
    void handle(TResult result);
}
