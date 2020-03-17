package com.qcmoke.pms.entity;

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
 * @since 2020-03-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_material_refund")
public class MaterialRefund implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 采购退货单主表编号
     */
    @TableId(value = "refund_id", type = IdType.AUTO)
    private Long refundId;

    /**
     * 采购订单主表编号
     */
    private Long purchaseOrderMasterId;

    /**
     * 退款渠道[1-支付宝，2-微信，3-银联，4-汇款]
     */
    private Integer refundChannel;

    /**
     * 总金额
     */
    private Double totalAmount;

    /**
     * 退货状态（1:退货中；2:退货成功；3:退货失败）
     */
    private Integer status;

    /**
     * 退货原因
     */
    private String reason;

    /**
     * 发货日期
     */
    private Date outDate;

    /**
     * 完成时间
     */
    private Date finishedTime;

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
     * 创建退单的用户id
     */
    private Long createUserId;


    /**
     * 审核状态（1:未审核；2:审核不通；3:审核通过）
     */
    private Integer checkStatus;

    /**
     * 最后一次审核的用户id
     */
    private Long checkUserId;
}
