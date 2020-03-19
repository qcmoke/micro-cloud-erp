package com.qcmoke.wms.vo;

import lombok.Data;

@Data
public class StockUpdateVo {
    /**
     * 库存编号
     */
    private Long stockId;

    /**
     * 库存最大值
     */
    private Integer sMax;

    /**
     * 存放地点
     */
    private String area;
}
