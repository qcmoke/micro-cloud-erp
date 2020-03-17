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
 * @since 2020-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_purchase_order_master")
public class PurchaseOrderMaster implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】
     */
    private Integer payType;

    /**
     * 支付状态【1:未支付；2:已支付】
     */
    private Integer payStatus;

    /**
     * 操作员id
     */
    private Long operatorId;

    /**
     * 运费
     */
    private Double freight;

    /**
     * 采购总金额(元)
     */
    private Double totalAmount;

    /**
     * 《入库状态》
     * 0:未提交入库申请；
     * 1:已提交申请未审核，
     * 2:审核不通过
     * 3:审核通过但未入库；
     * 4:审核通过并已入库
     */
    private Integer status;

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


    /**
     * 运费支付状态
     */
    private Integer freightPayStatus;

}
