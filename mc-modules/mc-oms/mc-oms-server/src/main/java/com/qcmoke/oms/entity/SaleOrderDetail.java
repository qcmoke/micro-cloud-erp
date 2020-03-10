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
@TableName("t_sale_order_detail")
public class SaleOrderDetail implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 销售订单明细表编号
     */
    @TableId(value = "detail_id", type = IdType.AUTO)
    private Long detailId;

    /**
     * 销售订单主表编号
     */
    private Long masterId;

    /**
     * 产品编号
     */
    private Long productId;

    /**
     * 客户编号
     */
    private Long customerId;

    /**
     * 单价
     */
    private Double price;

    /**
     * 数量
     */
    private Long count;

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
