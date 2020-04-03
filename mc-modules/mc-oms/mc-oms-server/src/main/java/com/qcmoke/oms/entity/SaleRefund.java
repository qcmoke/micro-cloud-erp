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
 * @since 2020-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_sale_refund")
public class SaleRefund implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 销售退单编号
     */
    @TableId(value = "refund_id", type = IdType.AUTO)
    private Long refundId;

    /**
     * 订单主表id
     */
    private Long saleOrderMasterId;

    /**
     * 退款渠道支付方式支付方式【1->支付宝；2->微信；3->银联】
     */
    private Integer refundChannel;

    /**
     * 退款金额
     */
    private Double totalAmount;

    /**
     * 退货时间
     */
    private Date refundDate;

    /**
     * 退货原因
     */
    private String reason;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 删除状态
     */
    private Integer deleteStatus;

    /**
     * 仓库审核状态（1：未审核；2：审核通过；3：审核不通过）
     */
    private Integer stockCheckStatus;

    /**
     * 入库状态（1：未入库；2：已入库）
     */
    private Integer stockInStatus;


}
