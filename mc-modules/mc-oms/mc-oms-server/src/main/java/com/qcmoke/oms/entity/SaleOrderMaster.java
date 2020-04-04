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
 * @since 2020-04-04
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
     * 支付方式【1->支付宝；2->微信；3->银联】
     */
    private Integer payType;

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
     * 收货人所在详细地址
     */
    private String receiverDetailAddress;

    /**
     * 运输渠道（物流公司）
     */
    private String deliveryChannel;

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
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 支付状态(包含运费)【1:未支付；2:已支付】
     */
    private Integer payStatus;

    /**
     * 仓库审核状态（1:未提交申请；2:已申请未审核；3：审核不通过；4：审核通过）
     */
    private Integer stockCheckStatus;

    /**
     * 发货状态（1：未发货；2：已发货）
     */
    private Integer outStatus;

    /**
     * 客户收货状态（1：未收货；2：已收货）
     */
    private Integer receiveStatus;

    /**
     * 完成状态（1：未完成；2：已完成）
     */
    private Integer finishStatus;

    /**
     * 删除状态【0->未删除；1->已删除】
     */
    private Integer deleteStatus;


}
