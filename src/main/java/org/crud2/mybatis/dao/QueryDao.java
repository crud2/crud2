package org.crud2.mybatis.dao;

import org.apache.ibatis.annotations.Mapper;
import org.crud2.query.QueryParameter;

import java.util.List;
import java.util.Map;

@Mapper
public interface QueryDao {
    List<Map<String, Object>> query(QueryParameter queryParameter);
}