package org.crud2.mybatis.dao;

import org.apache.ibatis.annotations.Mapper;
import org.crud2.edit.EditParameter;

@Mapper
public interface EditDao {
    int insertWithAutoId(EditParameter parameter);

    void update(EditParameter parameter);

    void delete(EditParameter parameter);
}
