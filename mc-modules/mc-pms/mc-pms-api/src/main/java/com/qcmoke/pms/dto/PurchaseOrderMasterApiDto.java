package com.qcmoke.pms.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author qcmoke
 */
@Accessors(chain = true)
@Data
public class PurchaseOrderMasterApiDto implements Serializable {
    private Long masterId;
    private String deliverySn;
    private String deliveryChannel;
}