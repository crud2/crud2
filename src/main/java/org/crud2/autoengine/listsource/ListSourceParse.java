package org.crud2.autoengine.listsource;

import org.crud2.util.RepeatableLinkedMap;

@FunctionalInterface
public interface ListSourceParse {
    RepeatableLinkedMap<String, Object> parse(String source, String valueType);
}
