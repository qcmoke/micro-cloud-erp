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
public interface MaterialRefundMapper extends BaseMapper<MaterialRefund> {

    @Select("   SELECT" +
            "       *," +
            "       purchase_order_master_id purchaseOrderMasterId," +
            "       CASE refund_channel" +
            "           WHEN 1 THEN '支付宝'" +
            "           WHEN 2 THEN '微信'" +
            "           WHEN 3 THEN '银联'" +
            "           ELSE NULL END refundChannelInfo" +
            "   FROM" +
            "       t_material_refund " +
            "   WHERE" +
            "       delete_status = 0")
    IPage<MaterialRefundVo> getPage(Page<MaterialRefund> page, MaterialRefund materialDto);
}
