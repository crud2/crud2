package org.crud2.autoengine.listsource;

import org.crud2.util.RepeatableLinkedMap;

import java.util.Map;

@FunctionalInterface
public interface ListSourceParse {
    RepeatableLinkedMap<String, Object> parse(String source, String valueType, Map<String,Object> parameters);
}
