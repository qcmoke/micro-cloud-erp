package com.qcmoke.fms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.fms.entity.Bill;
import com.qcmoke.fms.vo.BillVo;
import com.qcmoke.fms.vo.StatisticsDataItemVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-10
 */
@Repository
public interface BillMapper extends BaseMapper<Bill> {


    @Select("<script>" +
            "   SELECT" +
            "       <if  test = '!isOrderCount' > " +
            "       IFNULL(SUM(num),0) rs, " +
            "       </if>" +
            "       <if  test = 'isOrderCount' > " +
            "       IFNULL(COUNT(num),0) rs, " +
            "       </if>" +
            "       a.date dateStr" +
            "   FROM" +
            "     (" +
            "     SELECT" +
            "       CONCAT( #{year}, '-01' ) AS date UNION" +
            "     SELECT" +
            "       CONCAT( #{year}, '-02' ) AS date UNION" +
            "     SELECT" +
            "       CONCAT( #{year}, '-03' ) AS date UNION" +
            "     SELECT" +
            "       CONCAT( #{year}, '-04' ) AS date UNION" +
            "     SELECT" +
            "       CONCAT( #{year}, '-05' ) AS date UNION" +
            "     SELECT" +
            "       CONCAT( #{year}, '-06' ) AS date UNION" +
            "     SELECT" +
            "       CONCAT( #{year}, '-07' ) AS date UNION" +
            "     SELECT" +
            "       CONCAT( #{year}, '-08' ) AS date UNION" +
            "     SELECT" +
            "       CONCAT( #{year}, '-09' ) AS date UNION" +
            "     SELECT" +
            "       CONCAT( #{year}, '-10' ) AS date UNION" +
            "     SELECT" +
            "       CONCAT( #{year}, '-11' ) AS date UNION" +
            "     SELECT" +
            "       CONCAT( #{year}, '-12' ) AS date " +
            "     ) a " +
            "     LEFT JOIN " +
            "           (   SELECT " +
            "                   total_amount num ,create_time date " +
            "               from t_bill " +
            "               WHERE " +
            "                   delete_status =0 " +
            "                   <if  test = 'isIn' > " +
            "                       <if test = '!isOrderCount' > " +
            "                       AND (type = 2 OR type = 3)" +
            "                       </if>" +
            "                       <if test = 'isOrderCount' > " +
            "                       AND ( type = 3)" +
            "                       </if>" +
            "                   </if>" +
            "                   <if test = '!isIn' > " +
            "                   AND (type = 1 OR type = 4)" +
            "                   </if>" +
            "           ) b " +
            "       ON a.date = DATE_FORMAT( b.date, '%Y-%m' )" +
            "     GROUP BY  a.date" +
            "</script>")
    List<StatisticsDataItemVo> statistics(@Param("year") String year, @Param("isIn") boolean isIn, @Param("isOrderCount") boolean isOrderCount);


    @Select("   SELECT" +
            "     b.*," +
            "   CASE" +
            "       b.type " +
            "       WHEN 1 THEN" +
            "       '采购付款' " +
            "       WHEN 2 THEN" +
            "       '采购退货收款' " +
            "       WHEN 3 THEN" +
            "       '销售收款' " +
            "       WHEN 4 THEN" +
            "       '销售退款' ELSE NULL " +
            "     END typeInfo," +
            "     ta.account_name," +
            "     ta.bank_name," +
            "     ta.bank_num " +
            "   FROM" +
            "     t_bill b" +
            "     LEFT OUTER JOIN t_account ta ON b.account_id = ta.account_id " +
            "   WHERE" +
            "     b.delete_status = 0")
    IPage<BillVo> getPage(Page<Bill> page, @Param("bill") Bill bill);
}
