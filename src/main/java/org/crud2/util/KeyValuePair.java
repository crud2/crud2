package org.crud2.util;

import lombok.Data;

import java.io.Serializable;

@Data
public class KeyValuePair<Key,Value> implements Serializable {
    private Key key;
    private Value value;
}
