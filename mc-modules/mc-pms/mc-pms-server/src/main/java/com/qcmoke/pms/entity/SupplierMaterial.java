package com.qcmoke.pms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_supplier_material")
public class SupplierMaterial implements Serializable {

private static final long serialVersionUID=1L;

    @TableId(value = "supplier_material_id", type = IdType.AUTO)
    private Long supplierMaterialId;

    private Long supplierId;

    private Long materialId;


}
