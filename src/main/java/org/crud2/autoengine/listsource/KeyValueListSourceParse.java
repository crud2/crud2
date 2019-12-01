package org.crud2.autoengine.listsource;

import org.crud2.util.Convert;
import org.crud2.util.RepeatableLinkedMap;
import org.crud2.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * key-value like list source parse
 * eg source: 1:one;2:two;
 */
public class KeyValueListSourceParse implements ListSourceParse {
    private Logger logger = LoggerFactory.getLogger(KeyValueListSourceParse.class);

    @Override
    public RepeatableLinkedMap<String, Object> parse(String source, String valueType) {
        String[] tempPairs = source.split(";");
        RepeatableLinkedMap<String, Object> result = new RepeatableLinkedMap<>();
        for (String tempPairStr : tempPairs) {
            if (StringUtil.isNullOrEmpty(tempPairStr)) continue;
            String[] pair = tempPairStr.split(":");
            if (pair.length != 2) {
                logger.error(String.format("list-source define error : %s", source));
                break;
            }
            result.put(pair[1], Convert.toObject(pair[0], valueType));
        }
        return result;
    }
}
