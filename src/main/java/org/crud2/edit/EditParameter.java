package org.crud2.edit;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class EditParameter {
    private String table;
    private String key;
    private Object keyValue;
    private boolean identity = false;

    private Map<String, Object> values;

    public EditParameter() {
        values = new HashMap<>();
    }
}
