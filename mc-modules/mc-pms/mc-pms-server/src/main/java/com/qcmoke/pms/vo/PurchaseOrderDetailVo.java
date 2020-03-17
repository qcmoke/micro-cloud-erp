package com.qcmoke.pms.vo;

import com.qcmoke.pms.entity.Material;
import com.qcmoke.pms.entity.PurchaseOrderDetail;
import lombok.Data;

/**
 * @author qcmoke
 */
@Data
public class PurchaseOrderDetailVo extends PurchaseOrderDetail {

    /**
     * 原料名称
     */
    private String materialName;


    /**
     * 原料
     */
    private Material material;
}
