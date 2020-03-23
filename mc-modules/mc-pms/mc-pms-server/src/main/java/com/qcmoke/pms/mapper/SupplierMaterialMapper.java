package com.qcmoke.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcmoke.pms.entity.Material;
import com.qcmoke.pms.entity.SupplierMaterial;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-23
 */
@Repository
public interface SupplierMaterialMapper extends BaseMapper<SupplierMaterial> {

    @Select("   SELECT" +
            "       m.* " +
            "   FROM" +
            "       t_material m," +
            "       t_supplier_material sm " +
            "   WHERE" +
            "       m.material_id = sm.material_id " +
            "       AND m.delete_status = 0 " +
            "       AND sm.delete_status = 0 " +
            "       AND sm.supplier_id = #{supplierId}")
    List<Material> getMaterialListBySupplierId(@Param("supplierId") Long supplierId);
}
