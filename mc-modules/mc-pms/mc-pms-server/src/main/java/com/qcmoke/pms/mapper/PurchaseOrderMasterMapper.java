package com.qcmoke.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.pms.entity.PurchaseOrderMaster;
import com.qcmoke.pms.vo.PurchaseOrderMasterVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
@Repository
public interface PurchaseOrderMasterMapper extends BaseMapper<PurchaseOrderMaster> {



    @Select("   SELECT " +
            "       pm.*," +
            "       IF( pm.pay_status = 1, '未支付', '已支付' ) AS pay_status_info," +
            "       pm.master_id AS master_id2," +
            "       pm.supplier_id AS supplier_id2," +
            "   CASE" +
            "           pm.pay_type " +
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
            "           pm.status " +
            "           WHEN 1 THEN" +
            "           '未提交入库申请' " +
            "           WHEN 2 THEN" +
            "           '已提交申请但未审核' " +
            "           WHEN 3 THEN" +
            "           '审核不通过' " +
            "           WHEN 4 THEN" +
            "           '审核通过' " +
            "           WHEN 4 THEN" +
            "           '未入库' " +
            "           WHEN 6 THEN" +
            "           '已入库' ELSE NULL " +
            "       END statusInfo," +
            "   CASE" +
            "           pm.transfer_stock_status " +
            "           WHEN 1 THEN" +
            "           '未移交' " +
            "           WHEN 2 THEN" +
            "           '已移交申请' " +
            "           WHEN 3 THEN" +
            "           '移交失败' " +
            "           WHEN 4 THEN" +
            "           '已完成移交' ELSE NULL " +
            "       END transferStockStatusInfo" +
            "   FROM" +
            "       `t_purchase_order_master` pm " +
            "   WHERE" +
            "       pm.delete_status = 0 " +
            "       AND NOT EXISTS ( SELECT tr.purchase_order_master_id FROM t_material_refund tr WHERE tr.purchase_order_master_id = pm.master_id and tr.delete_status = 0) " +
            "   ORDER BY" +
            "       pm.create_time," +
            "       pm.modify_time," +
            "       pm.`status`")
    @Results({
            @Result(property = "supplier", column = "supplier_id2", one = @One(select = "com.qcmoke.pms.mapper.SupplierMapper.selectById", fetchType = FetchType.LAZY)),
            @Result(property = "purchaseOrderDetailVoSet", column = "master_id2", many = @Many(select = "com.qcmoke.pms.mapper.PurchaseOrderDetailMapper.getListByMasterId"))
    })
    IPage<PurchaseOrderMasterVo> selectPurchaseOrderMasterVoPage(Page<PurchaseOrderMaster> page);

    @Select("   SELECT pm.*," +
            "       IF( pm.pay_status = 1, '未支付', '已支付' ) AS pay_status_info," +
            "       pm.master_id AS master_id2," +
            "       pm.supplier_id AS supplier_id2 " +
            "   FROM" +
            "   `t_purchase_order_master` pm " +
            "   WHERE" +
            "       pm.`status` != 0" +
            "       AND pm.delete_status = 0 " +
            "       AND NOT EXISTS ( SELECT tr.purchase_order_master_id FROM t_material_refund tr WHERE tr.purchase_order_master_id = pm.master_id and tr.delete_status = 0) " +
            "   ORDER BY" +
            "       pm.create_time," +
            "       pm.modify_time," +
            "       pm.`status`")
    @Results({
            @Result(property = "supplier", column = "supplier_id2", one = @One(select = "com.qcmoke.pms.mapper.SupplierMapper.selectById")),
            @Result(property = "purchaseOrderDetailVoSet", column = "master_id2", many = @Many(select = "com.qcmoke.pms.mapper.PurchaseOrderDetailMapper.getListByMasterId"))
    })
    IPage<PurchaseOrderMasterVo> selectPurchaseOrderMasterVoPageForAddStock(Page<PurchaseOrderMaster> page);

}