package org.crud2.util;
@FunctionalInterface
public interface GetStringHander<T> {
    String getString(T t,int index);
}
