package org.crud2.edit.impl.inner;

@FunctionalInterface
public interface FixSQLCommandHander<T> {
    String getSql(T t);
}
