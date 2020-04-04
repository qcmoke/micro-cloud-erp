package com.qcmoke.pms.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.qcmoke.pms.entity.PurchaseOrderMaster;
import com.qcmoke.pms.entity.Supplier;
import lombok.Data;

import java.util.Set;

/**
 * @author qcmoke
 */
@Data
@JsonIgnoreProperties(value = {"handler"}) //避免使用懒加载时返回转换handler成json报错的问题
public class PurchaseOrderMasterVo extends PurchaseOrderMaster {
    /**
     * 供应商id
     */
    private Supplier supplier;

    /**
     * 对应的订单明细
     */
    private Set<PurchaseOrderDetailVo> purchaseOrderDetailVoSet;

    /**
     * 操作员id
     */
    private String operator;

    private String payTypeInfo;


    /**
     * 支付状态(包含运费)【1:未支付；2:已支付】
     */
    private String payStatusInfo;


    /**
     * 采购审批状态（1:未提交申请；2:已提交申请但未审核；3:审核不通过；4:审核通过）
     * 由采购员提交申请；采购管理员审批
     */
    private String purchaseCheckStatusInfo;

    /**
     * 仓库审核状态（1:未提交申请；2:已申请未审核；3：审核不通过；4：审核通过）
     */
    private String stockCheckStatusInfo;

    /**
     * 收货入库状态（1：未入库；2：已入库）
     */
    private String inStatusInfo;

    /**
     * 客户收货状态（1：未收货；2：已收货）
     */
    private String receiveStatusInfo;

    /**
     * 完成状态（1：未完成；2：已完成）
     */
    private String finishStatusInfo;

}
