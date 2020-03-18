package com.qcmoke.wms.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author qcmoke
 */
@Data
public class StockItemDto implements Serializable {

    /**
     * 订单编号
     */
    private Long orderId;

    /**
     * 备注
     */
    private String remark;

    private List<StockItemDetailDto> stockItemDetailDtoList;

}
