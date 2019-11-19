package org.crud2.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtil {
    public static Map<String, Object> toLowerCaseKeyMap(Map<String, Object> origin) {
        if (origin == null || origin.isEmpty()) return origin;
        Map<String, Object> result = new HashMap<String, Object>();
        for (String key : origin.keySet()) {
            result.put(key.toLowerCase(), origin.get(key));
        }
        return result;
    }

    public static List<Map<String, Object>> toLowerCaseKeyMapList(List<Map<String, Object>> list) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        list.forEach(t -> {
            resultList.add(toLowerCaseKeyMap(t));
        });
        return resultList;
    }
}
