package com.qcmoke.pms.dto;

import com.qcmoke.pms.entity.Material;
import com.qcmoke.pms.entity.PurchaseOrderDetail;

import java.io.Serializable;

/**
 * @author qcmoke
 */
public class PurchaseOrderDetailDto extends PurchaseOrderDetail implements Serializable {
    /**
     * 原料
     */
    private Material material;
}
