package org.crud2.autoengine.plugin.tree;

import com.alibaba.fastjson.JSON;
import org.crud2.autoengine.AutoEngine;
import org.crud2.autoengine.config.ModuleConfigReader;
import org.crud2.autoengine.config.ModuleDefineFactory;
import org.crud2.autoengine.plugin.Plugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.crud2.autoengine.config.Module;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component("treePlugin")
public class TreePlugin implements Plugin<TreeNode> {

    @Autowired
    ModuleDefineFactory moduleDefineFactory;

    @Override
    public List<TreeNode> query(Module module, Map<String, Object> params) {
        if (!(module instanceof TreeModule)) {
            String originalConfig = moduleDefineFactory.getOrignalConfig(module.getId());
            ModuleConfigReader reader = configString -> JSON.parseObject(configString, TreeModule.class);
            module = reader.read(originalConfig);
            moduleDefineFactory.regModule(module);
        }
        TreeModule treeModule = (TreeModule) module;
        List<Map<String, Object>> data = AutoEngine.queryListMap(module.getId(), params);
        Map<Object, Map<String, Object>> dataMap = initTreeData(treeModule, data);
        List<TreeNode> treeNodes = new ArrayList<>();
        buildTreeNodes(treeModule, dataMap, null, treeNodes);
        return treeNodes;

    }

    private Map<Object, Map<String, Object>> initTreeData(TreeModule treeModule, List<Map<String, Object>> data) {
        Map<Object, Map<String, Object>> map = new LinkedHashMap<>();
        data.forEach(t -> {
            map.put(t.get(treeModule.getIdField()), t);
        });
        return map;
    }

    private List<Object> getChildren(TreeModule treeModule, TreeNode parentNode, Map<Object, Map<String, Object>> data) {
        List<Object> result = new ArrayList<>();
        data.forEach((k, v) -> {
            if (parentNode == null) { //top nodes
                if (!data.containsKey(v.get(treeModule.getParentIdField()))) {
                    result.add(k);
                }
            } else {
                if (parentNode.getId().equals(v.get(treeModule.getParentIdField()))) {
                    result.add(k);
                }
            }
        });
        return result;
    }

    private void buildTreeNodes(TreeModule treeModule, Map<Object, Map<String, Object>> data, TreeNode parentNode, List<TreeNode> treeNodes) {
        List<Object> childIds = getChildren(treeModule, parentNode, data);
        childIds.forEach(o -> {
            Map<String, Object> nodeData = data.get(o);
            TreeNode node = new TreeNode();
            node.setId(o);
            node.setParentId(nodeData.get(treeModule.getParentIdField()));
            node.setText(nodeData.get(treeModule.getTextField()).toString());
            node.setNodeData(nodeData);
            if (parentNode == null)
                treeNodes.add(node);
            else {
                List<TreeNode> children = parentNode.getChildren();
                if (children == null) {
                    children = new ArrayList<>();
                    parentNode.setChildren(children);
                }
                children.add(node);
            }
            buildTreeNodes(treeModule, data, node, null);
        });
    }
}
