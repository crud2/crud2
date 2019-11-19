package org.crud2.autoengine.config;

import lombok.Data;

/***
 * column config
 * eg:
 * {
 *     "defaultValueType": 2,
 *     "required": 1,
 *     "sortType": "int",
 *     "key": 1,
 *     "name": "id"
 *   },
 */
@Data
public class Column {
    private String name;
    private int key;
    private String sortType;
    private int required;
    private int defaultValueType;
}
