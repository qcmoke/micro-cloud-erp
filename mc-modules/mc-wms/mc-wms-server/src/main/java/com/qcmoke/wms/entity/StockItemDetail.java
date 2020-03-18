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
@TableName("t_stock_item_detail")
public class StockItemDetail implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 出入库明细单编号
     */
    @TableId(value = "stock_item_detail_id", type = IdType.AUTO)
    private Long stockItemDetailId;

    /**
     * 出入库单编号
     */
    private Long stockItemId;

    /**
     * 货物编号
     */
    private Long itemId;

    /**
     * 数量
     */
    private Double itemNum;

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
