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
            "           so.`pay_status` " +
            "           WHEN 1 THEN" +
            "           '未支付' " +
            "           WHEN 2 THEN" +
            "           '已支付'" +
            "           ELSE NULL " +
            "       END payStatusInfo," +
            "   CASE" +
            "           so.stock_check_status " +
            "           WHEN 4 THEN" +
            "           '审核通过' " +
            "           WHEN 3 THEN" +
            "           '审核不通过' " +
            "           WHEN 2 THEN" +
            "           '已申请未审核' " +
            "           WHEN 1 THEN" +
            "           '未提交申请'" +
            "           ELSE NULL " +
            "       END stockCheckStatusInfo," +
            "   CASE" +
            "           so.`out_status` " +
            "           WHEN 1 THEN" +
            "           '未发货' " +
            "           WHEN 2 THEN" +
            "           '已发货'  " +
            "           ELSE NULL " +
            "       END outStatusInfo," +
            "   CASE" +
            "           so.`receive_status` " +
            "           WHEN 1 THEN" +
            "           '未完成' " +
            "           WHEN 2 THEN" +
            "           '已完成'  " +
            "           ELSE NULL " +
            "       END receiveStatusInfo," +
            "   CASE" +
            "           so.`finish_status` " +
            "           WHEN 1 THEN" +
            "           '未完成' " +
            "           WHEN 2 THEN" +
            "           '已完成'  " +
            "           ELSE NULL " +
            "       END finishStatusInfo," +
            "   CASE" +
            "           so.pay_type " +
            "           WHEN 1 THEN" +
            "           '支付宝' " +
            "           WHEN 2 THEN" +
            "           '微信' " +
            "           WHEN 3 THEN" +
            "           '银联' " +
            "           ELSE NULL " +
            "       END payTypeInfo," +
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
            "       so.delete_status = 0" +
            "       AND NOT EXISTS (  SELECT tr.sale_order_master_id FROM t_sale_refund tr WHERE tr.sale_order_master_id = so.master_id) ")
    @Results({
            @Result(property = "details", column = "master_id2", many = @Many(select = "com.qcmoke.oms.mapper.SaleOrderDetailMapper.getListByMasterId"))
    })
    IPage<SaleOrderMasterVo> getPage(Page<SaleOrderMaster> page, @Param("orderMasterDto") OrderMasterDto orderMasterDto);

}
