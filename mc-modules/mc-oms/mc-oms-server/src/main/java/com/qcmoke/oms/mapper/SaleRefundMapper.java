package com.qcmoke.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.oms.dto.SaleRefundQuery;
import com.qcmoke.oms.entity.SaleRefund;
import com.qcmoke.oms.vo.SaleRefundVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
public interface SaleRefundMapper extends BaseMapper<SaleRefund> {

    @Select(" <script>" +
            "   SELECT *," +
            "       CASE refund_channel" +
            "           WHEN 1 THEN '支付宝'" +
            "           WHEN 2 THEN '微信'" +
            "           WHEN 3 THEN '银联'" +
            "           ELSE NULL END refundChannelInfo," +
            "      CASE" +
            "              stock_check_status " +
            "              WHEN 1 THEN" +
            "              '未审核' " +
            "              WHEN 2 THEN" +
            "              '审核通过' " +
            "              WHEN 3 THEN" +
            "              '审核不通过' " +
            "          ELSE NULL END stockCheckStatusInfo," +
            "      CASE" +
            "              stock_in_status " +
            "              WHEN 1 THEN" +
            "              '未入库' " +
            "              WHEN 2 THEN" +
            "              '已入库' " +
            "          ELSE NULL END stockInStatusInfo " +
            "    FROM t_sale_refund " +
            "    where delete_status = 0" +
            "    <if test='query.createTimeFrom != null and query.createTimeTo != null'>" +
            "        AND create_time &gt;= #{query.createTimeFrom} AND create_time &lt;= #{query.createTimeTo} " +
            "    </if>" +
            " </script>")
    IPage<SaleRefundVo> getPage(Page<SaleRefund> page, @Param("query") SaleRefundQuery query);

}
