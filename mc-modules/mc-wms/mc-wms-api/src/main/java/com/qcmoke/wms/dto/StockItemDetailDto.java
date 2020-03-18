package com.qcmoke.wms.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qcmoke
 */

@Data
public class StockItemDetailDto implements Serializable {
    /**
     * 出入库单编号
     */
    private Long stockItemId;

    /**
     * 货物编号
     */
    private Long itemId;

    /**
     * 数量
     */
    private Double itemNum;

}
