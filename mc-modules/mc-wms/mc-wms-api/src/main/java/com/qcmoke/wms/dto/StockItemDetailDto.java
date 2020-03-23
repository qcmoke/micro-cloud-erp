package com.qcmoke.wms.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author qcmoke
 */
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
public class StockItemDetailDto implements Serializable {
    /**
     * 出入库单编号(主表id)
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
