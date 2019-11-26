package org.crud2.jdbc;

@FunctionalInterface
public interface ColumnKeyNameResolver {
    String resolve(String orignal);
}
