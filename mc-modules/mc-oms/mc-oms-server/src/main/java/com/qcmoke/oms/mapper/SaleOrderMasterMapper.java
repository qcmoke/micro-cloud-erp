package com.qcmoke.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.oms.dto.OrderMasterDto;
import com.qcmoke.oms.entity.SaleOrderMaster;
import com.qcmoke.oms.vo.SaleOrderMasterVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-15
 */
@Repository
public interface SaleOrderMasterMapper extends BaseMapper<SaleOrderMaster> {
    @Select("   SELECT" +
            "       so.*," +
            "       so.master_id master_id2," +
            "       cus.customer_name," +
            "   CASE" +
            "           so.`status` " +
            "           WHEN 1 THEN" +
            "           '待付款' " +
            "           WHEN 2 THEN" +
            "           '待发货' " +
            "           WHEN 3 THEN" +
            "           '已发货' " +
            "           WHEN 4 THEN" +
            "           '未确认收货' " +
            "           WHEN 5 THEN" +
            "           '已确认收货' " +
            "           WHEN 6 THEN" +
            "           '已完成' " +
            "           WHEN 7 THEN" +
            "           '已关闭' " +
            "           WHEN -1 THEN" +
            "           '无效订单' ELSE NULL " +
            "       END statusInfo," +
            "   CASE" +
            "           so.pay_type " +
            "           WHEN 1 THEN" +
            "           '支付宝' " +
            "           WHEN 2 THEN" +
            "           '微信' " +
            "           WHEN 3 THEN" +
            "           '银联' " +
            "           WHEN 4 THEN" +
            "           '货到付款' ELSE NULL " +
            "       END payTypeInfo," +
            "   CASE" +
            "           so.transfer_stock_status " +
            "           WHEN 4 THEN" +
            "           '已完成移交' " +
            "           WHEN 3 THEN" +
            "           '移交失败' " +
            "           WHEN 2 THEN" +
            "           '已移交申请' " +
            "           WHEN 1 THEN" +
            "           '未移交申请' ELSE NULL " +
            "       END transferStockStatusInfo," +
            "   CASE" +
            "           so.bill_type " +
            "           WHEN 1 THEN" +
            "           '不开发票' " +
            "           WHEN 2 THEN" +
            "           '电子发票' " +
            "           WHEN 3 THEN" +
            "           '纸质发票' " +
            "       END billTypeInfo " +
            "   FROM" +
            "       t_sale_order_master so" +
            "       LEFT JOIN t_customer cus ON ( so.customer_id = cus.customer_id ) " +
            "   WHERE" +
            "       so.delete_status = 0")
    @Results({
            @Result(property = "details", column = "master_id2", many = @Many(select = "com.qcmoke.oms.mapper.SaleOrderDetailMapper.getListByMasterId"))
    })
    IPage<SaleOrderMasterVo> getPage(Page<SaleOrderMaster> page, @Param("orderMasterDto") OrderMasterDto orderMasterDto);

}
