package org.crud2.autoengine.plugin.tree;

import lombok.Data;
import org.crud2.autoengine.config.Module;

@Data
public class TreeModule extends Module {
    private String idField;
    private String parentIdField;
    private String textField;
}
