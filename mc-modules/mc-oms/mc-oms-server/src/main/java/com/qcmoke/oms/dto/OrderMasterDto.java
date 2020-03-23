package com.qcmoke.oms.dto;

import com.qcmoke.oms.entity.SaleOrderDetail;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author qcmoke
 */
@Data
public class OrderMasterDto implements Serializable {
    private List<SaleOrderDetail> details;

    /**
     * 是否完成支付
     */
    private Boolean isPayStatus;

    /**
     * 销售订单主表编号
     */
    private Long masterId;

    /**
     * 客户编号
     */
    private Long customerId;


    /**
     * 订单总金额
     */
    private Double totalAmount;

    /**
     * 运费金额
     */
    private Double freightAmount;

    /**
     * 支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】
     */
    private Integer payType;


    /**
     * 收货人确认收货时间
     */
    private Date receiveTime;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话号码
     */
    private String receiverPhone;

    /**
     * 收货人所在详细地址
     */
    private String receiverDetailAddress;

    /**
     * 物流单号
     */
    private String deliverySn;

    /**
     * 发票类型[1->不开发票；2->电子发票；3->纸质发票]
     */
    private Integer billType;

    /**
     * 发票内容
     */
    private String billContent;


    /**
     * 订单备注
     */
    private String remark;
}
