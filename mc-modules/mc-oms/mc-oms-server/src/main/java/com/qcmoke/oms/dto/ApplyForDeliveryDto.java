package com.qcmoke.oms.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qcmoke
 */
@Data
public class ApplyForDeliveryDto implements Serializable {

    /**
     * 销售订单主表编号
     */
    private Long masterId;

}
