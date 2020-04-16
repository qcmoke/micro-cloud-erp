package com.qcmoke.pms.dto;

import lombok.Data;

/**
 * @author qcmoke
 */
@Data
public class PurchaseOrderMasterQuery {
    private Boolean isCheckRequest = false;
    private String createTimeFrom;
    private String createTimeTo;
}
