package org.crud2.query;

import lombok.Data;
import org.crud2.query.condition.Condition;

import java.util.List;

@Data
public class QueryParameter {
    private List<Condition> conditions;
    private String[] queryFields;
    private String queryTable;
    private String sql;
    /***
     * 当前查询第几页，从1开始
     */
    private int pageIndex;
    /***
     * 每页显示多少条数据
     */
    private int pageSize;

    /***
     * 跳过多少条记录
     */
    private int offset;

    /***
     * 本次获取多少条记录
     */
    private int limit;

    private String pageType = PAGE_TYPE_SIZE_INDEX;

    public final static String PAGE_TYPE_SIZE_INDEX = "size-index";
    public final static String PAGE_TYPE_OFFSET_LIMIT = "offset-limit";
}
