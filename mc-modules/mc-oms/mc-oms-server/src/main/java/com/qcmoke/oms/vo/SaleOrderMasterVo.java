package com.qcmoke.oms.vo;

import com.qcmoke.oms.entity.SaleOrderMaster;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author qcmoke
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SaleOrderMasterVo extends SaleOrderMaster {
    private List<SaleOrderDetailVo> details;
    /**
     * 客户姓名
     */
    private String customerName;
    /**
     * 支付方式
     */
    private String payTypeInfo;

    /**
     * 发票类型
     */
    private String billTypeInfo;


    /**
     * 支付状态(包含运费)【1:未支付；2:已支付】
     */
    private String payStatusInfo;

    /**
     * 仓库审核状态（1:未提交申请；2:已申请未审核；3：审核不通过；4：审核通过）
     */
    private String stockCheckStatusInfo;

    /**
     * 发货状态（1：未发货；2：已发货）
     */
    private String outStatusInfo;

    /**
     * 客户收货状态（1：未收货；2：已收货）
     */
    private String receiveStatusInfo;

    /**
     * 完成状态（1：未完成；2：已完成）
     */
    private String finishStatusInfo;
}
