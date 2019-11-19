package org.crud2.query.result;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class ArrayListData implements Serializable {
    private String[] keys;
    private Object[][] data;

    public void parse(List<Map<String, Object>> mapList) {
        if (mapList == null || mapList.size() == 0) return;
        Map<String, Object> first = mapList.get(0);
        keys = new String[first.size()];
        data = new Object[mapList.size()][];
        first.keySet().toArray(keys);
        parseData(mapList, keys);
    }

    public void parse(Map<String, String> keyNameMap, List<Map<String, Object>> mapList) {
        if (mapList == null || mapList.size() == 0) return;
        Map<String, Object> first = mapList.get(0);
        keys = new String[first.size()];
        data = new Object[mapList.size()][];
        Object[] orignalKeys = first.keySet().toArray();
        for (int i = 0; i < orignalKeys.length; i++) keys[i] = keyNameMap.get(orignalKeys[i]);
        parseData(mapList, orignalKeys);
    }

    private void parseData(List<Map<String, Object>> mapList, Object[] valueKeys) {
        for (int i = 0; i < mapList.size(); i++) {
            Map<String, Object> d = mapList.get(i);
            Object[] line = new Object[keys.length];
            for (int kIndex = 0; kIndex < keys.length; kIndex++) {
                line[kIndex] = d.get(valueKeys[kIndex]);
            }
            data[i] = line;
        }
    }
}
