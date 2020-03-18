package com.qcmoke.wms.entity;

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
 * @since 2020-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_stock_item")
public class StockItem implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 出入库单编号
     */
    @TableId(value = "stock_item_id", type = IdType.AUTO)
    private Long stockItemId;

    /**
     * 出入库类型(1:入库；2:出库)
     */
    private Integer stockType;

    /**
     * 货物类型（1:物料；2:产品）
     */
    private Integer itemType;

    /**
     * 库存管理员编号
     */
    private Long adminId;

    /**
     * 交接申请用户id
     */
    private Long applyUserId;

    /**
     * 库存管理员审核状态（1:未审核；2:审核不通过；3:审核通过）
     */
    private Integer checkStatus;

    /**
     * 完成出入库状态（1:未完成；2:已完成）
     */
    private Integer finishStatus;

    /**
     * 订单编号
     */
    private Long orderId;

    /**
     * 出入库时间
     */
    private Date makeDate;

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
