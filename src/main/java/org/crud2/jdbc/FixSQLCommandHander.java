package org.crud2.jdbc;

@FunctionalInterface
public interface FixSQLCommandHander<T> {
    String getSql(T t);
}
