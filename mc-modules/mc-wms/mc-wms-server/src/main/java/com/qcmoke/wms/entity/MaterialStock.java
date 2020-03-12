package com.qcmoke.wms.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("t_material_stock")
public class MaterialStock implements Serializable {

private static final long serialVersionUID=1L;

    /**
     * 原料库存编号
     */
    @TableId(value = "materiel_stock_id", type = IdType.AUTO)
    private Long materielStockId;

    /**
     * 原料编号
     */
    private Long materialId;

    /**
     * 原料名称
     */
    private String materialName;

    /**
     * 存放地点
     */
    private String area;

    /**
     * 库存量
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
     * 删除状态【0->未删除；1->已删除】
     */
    private Integer deleteStatus;


}
