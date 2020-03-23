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
 * @since 2020-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sale_order_master")
public class SaleOrderMaster implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 订单状态【1->待付款；2->待发货；3->已发货；4->未确认收货；5->已确认收货；6->已完成；7->已关闭；8->无效订单】
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
     * 删除状态【0->未删除；1->已删除】
     */
    private Integer deleteStatus;

    /**
     * 发货申请状态(1:未移交申请；2:已移交申请；3:移交失败；4:已完成移交；)
     */
    private Integer transferStockStatus;

}
