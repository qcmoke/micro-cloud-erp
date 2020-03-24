package com.qcmoke.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.wms.entity.StockItem;
import com.qcmoke.wms.vo.StockItemVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-18
 */
@Repository
public interface StockItemMapper extends BaseMapper<StockItem> {

    /**
     *
     出入库类型(1:采购入库；2:销售出库；3:采购退货出库；4:销售退货入库)
     * @param page
     * @param stockItemDto
     * @return
     */

    @Select("   SELECT  " +
            "     it.*,  " +
            "     it.stock_item_id AS stock_item_id2,  " +
            "     u.username AS applyUserName,  " +
            "     u2.username adminName,  " +
            "   CASE  " +
            "          it.stock_type   " +
            "          WHEN 1 THEN  " +
            "          '采购入库'   " +
            "          WHEN 2 THEN  " +
            "          '销售出库'   " +
            "          WHEN 3 THEN  " +
            "          '采购退货出库'   " +
            "          WHEN 4 THEN  " +
            "          '销售退货入库' ELSE NULL   " +
            "     END stockTypeInfo,  " +
            "   CASE  " +
            "          it.check_status   " +
            "          WHEN 1 THEN  " +
            "          '未审核'   " +
            "          WHEN 2 THEN  " +
            "          '审核不通过'   " +
            "          WHEN 3 THEN  " +
            "          '审核通过' ELSE NULL   " +
            "     END checkStatusInfo,  " +
            "   CASE  " +
            "          it.finish_status   " +
            "          WHEN 1 THEN  " +
            "          '未完成'   " +
            "          WHEN 2 THEN  " +
            "          '已完成' ELSE NULL   " +
            "     END finishStatusInfo,  " +
            "   CASE  " +
            "          it.item_type   " +
            "          WHEN 1 THEN  " +
            "          '物料'   " +
            "          WHEN 2 THEN  " +
            "          '产品' ELSE NULL   " +
            "     END itemTypeInfo   " +
            "   FROM  " +
            "     `t_stock_item` it  " +
            "     LEFT JOIN `mc-ums`.t_user u ON ( it.apply_user_id = u.user_id )  " +
            "     LEFT JOIN `mc-ums`.t_user u2 ON ( it.admin_id = u2.user_id )  " +
            "   WHERE    " +
            "     it.delete_status = 0")
    @Results({
            @Result(property = "stockItemDetailVos", column = "stock_item_id2", many = @Many(select = "com.qcmoke.wms.mapper.StockItemDetailMapper.getListByStockItemId"))
    })
    IPage<StockItemVo> getPage(Page<StockItem> page, @Param("stockItemDto") StockItem stockItemDto);
}
