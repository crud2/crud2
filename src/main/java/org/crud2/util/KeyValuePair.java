package org.crud2.util;

import lombok.Data;

@Data
public class KeyValuePair<Key,Value> {
    private Key key;
    private Value value;
}
