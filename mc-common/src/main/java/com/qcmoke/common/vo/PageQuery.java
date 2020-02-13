package com.qcmoke.common.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


/**
 * @author qcmoke
 */
@Data
@ToString
public class PageQuery implements Serializable {

    /**
     * 排序规则：降序
     */
    public static final String ORDER_DESC = "descending";
    /**
     * 排序规则：升序
     */
    public static final String ORDER_ASC = "ascending";


    /**
     * 当前页面数据量
     */
    private int pageSize = 10;
    /**
     * 当前页码
     */
    private int pageNum = 1;
    /**
     * 排序字段
     */
    private String field;
    /**
     * 排序规则，asc升序，desc降序
     */
    private String order;
}
