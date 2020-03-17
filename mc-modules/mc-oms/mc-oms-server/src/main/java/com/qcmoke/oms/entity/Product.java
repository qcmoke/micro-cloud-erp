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
 * @since 2020-03-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_product")
public class Product implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 产品编号
     */
    @TableId(value = "product_id", type = IdType.AUTO)
    private Long productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 批准文号
     */
    private String approveId;

    /**
     * 生产批号
     */
    private String batchId;

    /**
     * 销售单价(零售价)
     */
    private Double price;

    /**
     * 商品产地
     */
    private String space;

    /**
     * 供应商编号
     */
    private Long supplierId;

    /**
     * 单位
     */
    private String unit;

    /**
     * 规格
     */
    private String standard;

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
