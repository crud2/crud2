package org.crud2.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ClassNames {
    private static Map<String, String> nameMap = new HashMap<>();

    static {
        nameMap.put(Integer.class.getSimpleName(), "int");
        nameMap.put(String.class.getSimpleName(), "string");
        nameMap.put(Date.class.getSimpleName(), "date");
        nameMap.put(BigDecimal.class.getSimpleName(), "decimal");
        nameMap.put(Long.class.getSimpleName(), "long");
    }

    public static String simple(Object obj) {
        String name = obj.getClass().getSimpleName();
        return nameMap.getOrDefault(name, name);
    }
}
