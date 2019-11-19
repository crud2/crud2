package org.crud2.query.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class PagerSerializable implements Serializable {
    /***
     * datalist total count
     */
    private long total;
    /***
     * current page index
     */
    private int pageNum;
    /***
     * list count per page
     */
    private int pageSize;

    /***
     * total page count
     */
    private int pages;
}
