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
@TableName("t_stock")
public class Stock implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 库存编号
     */
    @TableId(value = "stock_id", type = IdType.AUTO)
    private Long stockId;

    /**
     * 货物类型（1:物料；2:产品）
     */
    private Integer itemType;

    /**
     * 产品编号
     */
    private Long itemId;

    /**
     * 库存数量
     */
    private Double itemCount;

    /**
     * 库存最大值
     */
    private Integer sMax;

    /**
     * 库存最小值
     */
    private Integer sMin;

    /**
     * 存放地点
     */
    private String area;

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
