package org.crud2.query.result;

@FunctionalInterface
public interface MapResultKeyNameResolver {
    String resolve(String orignal);
}
