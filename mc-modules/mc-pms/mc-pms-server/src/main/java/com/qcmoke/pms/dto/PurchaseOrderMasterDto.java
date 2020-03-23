package com.qcmoke.pms.dto;

import com.qcmoke.pms.entity.PurchaseOrderDetail;
import lombok.Data;

import java.util.List;

/**
 * @author qcmoke
 */
@Data
public class PurchaseOrderMasterDto {

    /**
     * 采购订单主表id
     */
    private Long masterId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 明细
     */
    private List<PurchaseOrderDetail> purchaseOrderDetailList;


    /**
     * 供应商id
     */
    private Long supplierId;


    /**
     * 支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】
     */
    private Integer payType;


    /**
     * 运费
     */
    private Double freight;


    /**
     * 是否已经支付
     */
    private Boolean isPayStatus;
}
