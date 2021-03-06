package org.crud2.util;

public class ArrayUtil {
    public static <T> String toString(T[] array, GetStringHander<T> hander) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            builder.append(hander.getString(array[i], i));
            if (i < array.length - 1) builder.append(",");
        }
        return builder.toString();
    }

    public static <T> String toString(T[] array) {
        return toString(array, (t, index) -> t.toString());
    }
}
