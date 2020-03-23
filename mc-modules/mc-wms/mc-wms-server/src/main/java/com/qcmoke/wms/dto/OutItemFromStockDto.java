package com.qcmoke.wms.dto;

import lombok.Data;

/**
 * @author qcmoke
 */
@Data
public class OutItemFromStockDto {
    private Long stockItemId;
    private String deliverySn;
    private String deliveryChannel;
}
