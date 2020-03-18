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


    @Select("   SELECT" +
            "     itd.*," +
            "     mat.material_name " +
            "   FROM" +
            "     t_stock_item_detail itd" +
            "     LEFT JOIN `mc-pms`.t_material mat ON ( itd.item_id = mat.material_id ) " +
            "   WHERE" +
            "     itd.delete_status = 0" +
            "     AND itd.stock_item_id = #{stockItemId}")
    List<StockItemDetailVo> getListByStockItemId(@PathParam("stockItemId") Long stockItemId);
}
