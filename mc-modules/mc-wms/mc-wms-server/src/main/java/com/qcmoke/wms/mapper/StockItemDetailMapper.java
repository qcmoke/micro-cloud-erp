package com.qcmoke.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcmoke.wms.entity.StockItemDetail;
import com.qcmoke.wms.vo.StockItemDetailVo;
import org.apache.ibatis.annotations.Select;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-18
 */
public interface StockItemDetailMapper extends BaseMapper<StockItemDetail> {


    @Select("   SELECT  " +
            "     std.*,  " +
            "     st.item_type,  " +
            "     s.item_count,  " +
            "   CASE  " +
            "       st.item_type   " +
            "       WHEN 1 THEN  " +
            "       tm.material_name   " +
            "       WHEN 2 THEN  " +
            "       tp.product_name ELSE NULL   " +
            "     END item_name,  " +
            "   CASE  " +
            "       st.item_type   " +
            "       WHEN 1 THEN  " +
            "       tm.img   " +
            "       WHEN 2 THEN  " +
            "       tp.img ELSE NULL   " +
            "     END img,   " +
            "   CASE  " +
            "       st.item_type   " +
            "       WHEN 1 THEN  " +
            "       tm.unit   " +
            "       WHEN 2 THEN  " +
            "       tp.unit ELSE NULL   " +
            "     END unit,   " +
            "   CASE  " +
            "       st.item_type   " +
            "       WHEN 1 THEN  " +
            "       tm.standard   " +
            "       WHEN 2 THEN  " +
            "       tp.standard ELSE NULL   " +
            "     END standard   " +
            "   FROM  " +
            "     `t_stock_item_detail` std  " +
            "     LEFT JOIN `mc-pms`.t_material tm ON ( tm.material_id = std.item_id )  " +
            "     LEFT JOIN `mc-oms`.t_product tp ON ( tp.product_id = std.item_id )  " +
            "     LEFT JOIN t_stock_item st ON ( st.stock_item_id = std.stock_item_id )  " +
            "     LEFT JOIN t_stock s ON (s.item_id = std.item_id AND st.item_type = s.item_type)    " +
            "   WHERE  " +
            "     std.delete_status = 0   " +
            "     AND std.stock_item_id = #{stockItemId}")
    List<StockItemDetailVo> getListByStockItemId(@PathParam("stockItemId") Long stockItemId);


}
