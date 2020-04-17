package com.qcmoke.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.wms.dto.StockQuery;
import com.qcmoke.wms.entity.Stock;
import com.qcmoke.wms.vo.StockVo;
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
 * @since 2020-03-18
 */
@Repository
public interface StockMapper extends BaseMapper<Stock> {

    @Select("   SELECT" +
            "         1 AS item_type," +
            "         m.material_id AS item_id," +
            "         m.safety_stock AS s_min," +
            "         now() createTime" +
            "   FROM" +
            "         `mc-pms`.t_material m " +
            "   WHERE" +
            "         m.delete_status = 0 " +
            "         AND m.material_id NOT IN (SELECT item_id FROM t_stock) " +
            "   UNION" +
            "   SELECT" +
            "         2 AS itme_type," +
            "         p.product_id AS item_id," +
            "         p.safety_stock AS s_min," +
            "         now() createTime" +
            "   FROM" +
            "         `mc-oms`.t_product p " +
            "   WHERE" +
            "         p.delete_status = 0" +
            "         AND p.product_id NOT IN (SELECT item_id FROM t_stock) ")
    List<Stock> getAllMaterialsAndProductsNotInStock();


    @Select(" <script>" +
            "   SELECT * FROM (" +
            "       SELECT" +
            "         st.*," +
            "       CASE" +
            "           st.item_type " +
            "           WHEN 1 THEN" +
            "           tm.material_name " +
            "           WHEN 2 THEN" +
            "           tp.product_name ELSE NULL " +
            "         END item_name " +
            "       FROM" +
            "         `t_stock` st" +
            "         LEFT JOIN `mc-pms`.t_material tm ON ( tm.material_id = st.item_id AND st.item_type = 1 )" +
            "         LEFT JOIN `mc-oms`.t_product tp ON ( tp.product_id = st.item_id AND st.item_type = 2 )" +
            "       WHERE" +
            "         st.delete_status = 0" +
            "   ) tb" +
            "   WHERE 1 = 1 " +
            "    <if test=\"query.itemNameKey != null and query.itemNameKey != ''\">" +
            "       AND tb.item_name like concat('%', #{query.itemNameKey}, '%')" +
            "    </if>" +
            " </script>")
    IPage<StockVo> getPage(Page<Stock> page, @Param("query") StockQuery query);
}
