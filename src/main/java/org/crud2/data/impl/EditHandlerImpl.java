package org.crud2.data.impl;

import org.crud2.edit.EditParameter;
import org.crud2.mybatis.dao.EditDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("editHandler")
public class EditHandlerImpl {
    @Autowired
    private EditDao editDao;
    /*

    @Override
    public Object insert(String table, Map<String, Object> data) {
        List<ColumnConfig> columnConfigs = tableDefine.getTable(table);
        ColumnConfig keyColumn = tableDefine.getKey(table);
        EditParameter insertParameter = new EditParameter();
        insertParameter.setTable(table);
        for (ColumnConfig config : columnConfigs) {
            if (config == keyColumn) continue;
            insertParameter.addParam(config.getName(), config.getSortType(), data.get(config.getName()));
        }
        return editDao.insertWithAutoId(insertParameter);


    }

    @Override
    public Object update(String table, Map<String, Object> data) {
        List<ColumnConfig> columnConfigs = tableDefine.getTable(table);
        ColumnConfig keyColumn = tableDefine.getKey(table);
        EditParameter editParameter = new EditParameter();
        editParameter.setTable(table);
        editParameter.setKey(keyColumn.getName());
        editParameter.setKeyValue(keyColumn.getSortType(), data.get(keyColumn.getName()));
        for (ColumnConfig config : columnConfigs) {
            if (config == keyColumn) continue;
            editParameter.addParam(config.getName(), config.getSortType(), data.get(config.getName()));
        }
        editDao.update(editParameter);
        return editParameter.getKeyValue();
    }

    @Override
    public void delete(String table, Map<String, Object> data) {
        ColumnConfig keyColumn = tableDefine.getKey(table);
        EditParameter editParameter = new EditParameter();
        editParameter.setTable(table);
        editParameter.setKey(keyColumn.getName());
        editParameter.setKeyValue(data.get(keyColumn.getName()));
        editDao.delete(editParameter);
    }
    */
}
