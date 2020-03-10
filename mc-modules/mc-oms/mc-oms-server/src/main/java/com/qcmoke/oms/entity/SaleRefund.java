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
     * 退款渠道[1-支付宝，2-微信，3-银联，4-汇款]
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


}
