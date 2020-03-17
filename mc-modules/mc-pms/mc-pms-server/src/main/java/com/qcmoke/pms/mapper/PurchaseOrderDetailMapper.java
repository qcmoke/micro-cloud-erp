package com.qcmoke.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcmoke.pms.entity.PurchaseOrderDetail;
import com.qcmoke.pms.vo.PurchaseOrderDetailVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * <pd>
 * Mapper 接口
 * </pd>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
public interface PurchaseOrderDetailMapper extends BaseMapper<PurchaseOrderDetail> {

    @Select("   SELECT" +
            "   pd.*,m.material_name,m.material_id as materialId2" +
            "   FROM" +
            "   t_purchase_order_detail pd" +
            "   LEFT JOIN t_material m ON ( m.material_id = pd.material_id ) " +
            "   WHERE" +
            "   pd.master_id = #{masterId} " +
            "   AND pd.delete_status = 0 ")
    @Results({
            @Result(property = "material", column = "materialId2", one = @One(select = "com.qcmoke.pms.mapper.MaterialMapper.selectById")),

    })
    List<PurchaseOrderDetailVo> getListByMasterId(@Param("masterId") Long masterId);
}
