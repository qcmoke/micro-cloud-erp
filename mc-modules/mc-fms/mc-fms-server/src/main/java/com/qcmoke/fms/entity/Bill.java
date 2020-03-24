package com.qcmoke.fms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("t_bill")
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账单编号
     */
    @TableId(value = "bill_id", type = IdType.AUTO)
    private Integer billId;

    /**
     * 账目类型(1:采购付款；2:采购退货收款；3:销售收款；4:销售退款)
     */
    private Integer type;

    /**
     * 交易单据编号(采购单，采购退货单，销售订单，销售退货单等编号)
     */
    private Long dealNum;

    /**
     * 账户编号
     */
    private Long accountId;

    /**
     * 合计金额
     */
    private Double totalAmount;

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
