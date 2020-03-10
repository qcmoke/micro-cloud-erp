package com.qcmoke.oms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sale_order_master")
public class SaleOrderMaster implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 销售订单主表编号
     */
    @TableId(value = "master_id", type = IdType.AUTO)
    private Long masterId;

    /**
     * 客户编号
     */
    private Long customerId;

    /**
     * 销售时间
     */
    private Date saleDate;

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
     * 订单状态【0->待付款；1->待发货；2->已发货；3->未确认收货；4->已确认收货；5->已完成；6->已关闭；7->无效订单】
     */
    private Integer status;

    /**
     * 发货时间
     */
    private Date deliveryTime;

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
     * 收货人邮编
     */
    private String receiverPostCode;

    /**
     * 收货人所在省
     */
    private String receiverProvince;

    /**
     * 收货人所在城市
     */
    private String receiverCity;

    /**
     * 收货人所在区
     */
    private String receiverRegion;

    /**
     * 收货人所在详细地址
     */
    private String receiverDetailAddress;

    /**
     * 物流单号
     */
    private String deliverySn;

    /**
     * 发票类型[0->不开发票；1->电子发票；2->纸质发票]
     */
    private Integer billType;

    /**
     * 发票内容
     */
    private String billContent;

    /**
     * 发票抬头
     */
    private String billHeader;

    /**
     * 收票人电话
     */
    private String billReceiverPhone;

    /**
     * 收票人邮箱
     */
    private String billReceiverEmail;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 删除状态【0->未删除；1->已删除】
     */
    private Integer deleteStatus;

    /**
     * 订单备注
     */
    private String note;


}
