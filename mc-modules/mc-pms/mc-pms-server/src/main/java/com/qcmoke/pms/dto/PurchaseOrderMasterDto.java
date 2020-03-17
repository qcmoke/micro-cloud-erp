package com.qcmoke.pms.dto;

import com.qcmoke.pms.entity.PurchaseOrderDetail;
import com.qcmoke.pms.entity.PurchaseOrderMaster;
import lombok.Data;

import java.util.List;

/**
 * @author qcmoke
 */
@Data
public class PurchaseOrderMasterDto extends PurchaseOrderMaster {
    private List<PurchaseOrderDetail> purchaseOrderDetailList;
    private Boolean freightIsPaid;
    private Boolean orderIsPaid;
}
