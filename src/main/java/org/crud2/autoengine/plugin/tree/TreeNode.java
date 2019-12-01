package org.crud2.autoengine.plugin.tree;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class TreeNode implements Serializable {
    private Object id;
    private Object parentId;
    private String text;
    private Map<String,Object> nodeData;
    private List<TreeNode> children;
}
