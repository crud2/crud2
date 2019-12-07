package org.crud2.core.query;

import lombok.Data;
import org.crud2.core.query.condition.Condition;

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

    private boolean pageConfiged = false;

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        pageConfiged = true;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
        pageConfiged = true;
    }

    public void setOffset(int offset) {
        this.offset = offset;
        pageConfiged = true;
    }

    public void setLimit(int limit) {
        this.limit = limit;
        pageConfiged = true;
    }

    private String pageType = PAGE_TYPE_SIZE_INDEX;

    public final static String PAGE_TYPE_SIZE_INDEX = "size-index";
    public final static String PAGE_TYPE_OFFSET_LIMIT = "offset-limit";
}
