package org.crud2.util;

import java.util.ArrayList;
import java.util.List;

/**
 * a map like data, but key can repeat
 *
 * @param <TKey>
 * @param <TValue>
 */
public class RepeatableLinkedMap<TKey, TValue> {

    ArrayList<KeyValuePair<TKey, TValue>> data;

    public RepeatableLinkedMap() {
        data = new ArrayList<>();
    }

    public void put(TKey key, TValue value) {
        KeyValuePair<TKey, TValue> keyValuePair = new KeyValuePair<>();
        keyValuePair.setKey(key);
        keyValuePair.setValue(value);
        data.add(keyValuePair);
    }

    /**
     * get first key-value pair matched key
     *
     * @param key
     * @return matched key-value pair,null result if not matched
     */
    public KeyValuePair<TKey, TValue> get(TKey key) {
        for (KeyValuePair<TKey, TValue> item : data) {
            if (item.getKey().equals(key)) return item;
        }
        return null;
    }

    /**
     * get first key-value pair matched value
     *
     * @param value
     * @return
     */
    public KeyValuePair<TKey, TValue> getByValue(TValue value) {
        for (KeyValuePair<TKey, TValue> item : data) {
            if (item.getValue().equals(value)) return item;
        }
        return null;
    }

    /**
     * get list key-value pair matched key
     *
     * @param key
     * @return key-value pair list matched key,empty list if not match
     */
    public List<KeyValuePair<TKey, TValue>> getList(TKey key) {
        List<KeyValuePair<TKey, TValue>> list = new ArrayList<>();
        for (KeyValuePair<TKey, TValue> item : data) {
            if (item.getKey().equals(key)) list.add(item);
        }
        return list;
    }

    /**
     * get list key-value pair matched value
     *
     * @param value
     * @return key-value pair list matched value,empty list if not match
     */
    public List<KeyValuePair<TKey, TValue>> getListByValue(TValue value) {
        List<KeyValuePair<TKey, TValue>> list = new ArrayList<>();
        for (KeyValuePair<TKey, TValue> item : data) {
            if (item.getValue().equals(value)) list.add(item);
        }
        return list;
    }

}
