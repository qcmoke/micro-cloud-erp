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
            "       pm.master_id AS master_id2," +
            "       pm.supplier_id AS supplier_id2," +
            "   CASE" +
            "           pm.`pay_status` " +
            "           WHEN 1 THEN" +
            "           '未支付' " +
            "           WHEN 2 THEN" +
            "           '已支付'" +
            "           ELSE NULL " +
            "       END payStatusInfo," +
            "   CASE" +
            "           pm.purchase_check_status " +
            "           WHEN 4 THEN" +
            "           '审核通过' " +
            "           WHEN 3 THEN" +
            "           '审核不通过' " +
            "           WHEN 2 THEN" +
            "           '已申请未审核' " +
            "           WHEN 1 THEN" +
            "           '未提交申请'" +
            "           ELSE NULL " +
            "       END purchaseCheckStatusInfo," +
            "   CASE" +
            "           pm.stock_check_status " +
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
            "           pm.`in_status` " +
            "           WHEN 1 THEN" +
            "           '未入库' " +
            "           WHEN 2 THEN" +
            "           '已入库'  " +
            "           ELSE NULL " +
            "       END inStatusInfo," +
            "   CASE" +
            "           pm.`finish_status` " +
            "           WHEN 1 THEN" +
            "           '未完成' " +
            "           WHEN 2 THEN" +
            "           '已完成'  " +
            "           ELSE NULL " +
            "       END finishStatusInfo," +
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
            "       END payTypeInfo" +
            "   FROM" +
            "       `t_purchase_order_master` pm " +
            "   WHERE" +
            "       pm.delete_status = 0 " +
            "       AND NOT EXISTS ( SELECT tr.purchase_order_master_id FROM t_material_refund tr WHERE tr.purchase_order_master_id = pm.master_id) " +
            "   ORDER BY" +
            "       pm.create_time," +
            "       pm.modify_time")
    @Results({
            @Result(property = "supplier", column = "supplier_id2", one = @One(select = "com.qcmoke.pms.mapper.SupplierMapper.selectById", fetchType = FetchType.LAZY)),
            @Result(property = "purchaseOrderDetailVoSet", column = "master_id2", many = @Many(select = "com.qcmoke.pms.mapper.PurchaseOrderDetailMapper.getListByMasterId"))
    })
    IPage<PurchaseOrderMasterVo> selectPurchaseOrderMasterVoPage(Page<PurchaseOrderMaster> page);

}