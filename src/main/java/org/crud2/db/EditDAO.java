package org.crud2.db;

import org.apache.ibatis.annotations.Mapper;
import org.crud2.edit.EditParameter;

@Mapper
public interface EditDAO {
    long insertIdentity(EditParameter parameter);

    void insert(EditParameter parameter);

    void update(EditParameter parameter);

    void delete(EditParameter parameter);
}
