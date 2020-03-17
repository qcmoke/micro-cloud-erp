package com.qcmoke.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.pms.entity.PurchaseOrderMaster;
import com.qcmoke.pms.vo.PurchaseOrderMasterVo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
public interface PurchaseOrderMasterMapper extends BaseMapper<PurchaseOrderMaster> {

    @Select("   SELECT " +
            "       pm.*," +
            "       IF( pm.pay_status = 1, '未支付', '已支付' ) AS pay_status_info," +
            "       pm.master_id AS master_id2," +
            "       pm.supplier_id AS supplier_id2 " +
            "   FROM" +
            "       `t_purchase_order_master` pm " +
            "   WHERE" +
            "       pm.delete_status = 0 " +
            "       AND NOT EXISTS ( SELECT tr.purchase_order_master_id FROM t_material_refund tr WHERE tr.purchase_order_master_id = pm.master_id ) " +
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
            "       AND NOT EXISTS ( SELECT tr.purchase_order_master_id FROM t_material_refund tr WHERE tr.purchase_order_master_id = pm.master_id ) " +
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