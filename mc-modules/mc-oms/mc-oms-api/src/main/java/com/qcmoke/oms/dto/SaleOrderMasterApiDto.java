package com.qcmoke.oms.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author qcmoke
 */
@Accessors(chain = true)
@Data
public class SaleOrderMasterApiDto implements Serializable {
    private Long masterId;
    private String deliverySn;
    private String deliveryChannel;
}
