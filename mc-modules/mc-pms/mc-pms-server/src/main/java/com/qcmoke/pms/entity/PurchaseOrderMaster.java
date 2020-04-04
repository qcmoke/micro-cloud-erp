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
 * @since 2020-04-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_purchase_order_master")
public class PurchaseOrderMaster implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 采购订单主表id
     */
    @TableId(value = "master_id", type = IdType.AUTO)
    private Long masterId;

    /**
     * 供应商id
     */
    private Long supplierId;

    /**
     * 采购时间
     */
    private Date purchaseDate;

    /**
     * 支付方式【1->支付宝；2->微信；3->银联】
     */
    private Integer payType;

    /**
     * 采购总金额(元)
     */
    private Double totalAmount;

    /**
     * 运费金额
     */
    private Double freight;

    /**
     * 操作员id(最后一次修改的操作员)
     */
    private Long operatorId;

    /**
     * 支付状态(包含运费)【1:未支付；2:已支付】
     */
    private Integer payStatus;

    /**
     * 采购审批状态（1:未提交申请；2:已提交申请但未审核；3:审核不通过；4:审核通过）
     * 由采购员提交申请；采购管理员审批
     */
    private Integer purchaseCheckStatus;

    /**
     * 仓库审核状态（1:未提交申请；2:已申请未审核；3：审核不通过；4：审核通过）
     * 由采购管理员提交申请；仓库管理员审批
     */
    private Integer stockCheckStatus;

    /**
     * 收货入库状态（1：未入库；2：已入库）
     */
    private Integer inStatus;

    /**
     * 完成状态（1：未完成；2：已完成）
     */
    private Integer finishStatus;

    /**
     * 备注
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


}
