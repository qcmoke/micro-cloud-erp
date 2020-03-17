package com.qcmoke.pms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.pms.entity.MaterialRefund;
import com.qcmoke.pms.vo.MaterialRefundVo;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
public interface MaterialRefundMapper extends BaseMapper<MaterialRefund> {

    @Select("   SELECT" +
            "       *," +
            "       purchase_order_master_id purchaseOrderMasterId," +
            "       CASE status" +
            "           WHEN 1 THEN '退货中'" +
            "           WHEN 2 THEN '退货成功'" +
            "           WHEN 3 THEN '退货失败'" +
            "           ELSE '未退货' END statusInfo," +
            "       CASE refund_channel" +
            "           WHEN 1 THEN '支付宝'" +
            "           WHEN 2 THEN '微信'" +
            "           WHEN 3 THEN '银联'" +
            "           WHEN 4 THEN '汇款'" +
            "           ELSE NULL END refundChannelInfo," +
            "       CASE check_status" +
            "           WHEN 1 THEN '未审核'" +
            "           WHEN 2 THEN '审核不通'" +
            "           WHEN 3 THEN '审核通过'" +
            "           ELSE NULL END checkStatusInfo" +
            "   FROM" +
            "       t_material_refund " +
            "   WHERE" +
            "       delete_status = 0")
    @Results({
            @Result(property = "purchaseOrderMaster", column = "purchaseOrderMasterId", one = @One(select = "com.qcmoke.pms.mapper.PurchaseOrderMasterMapper.selectById")),
    })
    IPage<MaterialRefundVo> getPage(Page<MaterialRefund> page, MaterialRefund materialDto);
}
