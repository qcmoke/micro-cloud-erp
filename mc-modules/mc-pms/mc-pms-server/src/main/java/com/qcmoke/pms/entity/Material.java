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
@TableName("t_material")
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 物料编号
     */
    @TableId(value = "material_id", type = IdType.AUTO)
    private Long materialId;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 图片
     */
    private String img;

    /**
     * 单位
     */
    private String unit;

    /**
     * 规格
     */
    private String standard;

    /**
     * 采购单价
     */
    private Double price;

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
     * 安全库存
     */
    private Double safetyStock;

    /**
     * 备注
     */
    private String remark;


}
