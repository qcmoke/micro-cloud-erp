package com.qcmoke.oms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcmoke.oms.entity.SaleOrderDetail;
import com.qcmoke.oms.vo.SaleOrderDetailVo;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-15
 */
@Repository
public interface SaleOrderDetailMapper extends BaseMapper<SaleOrderDetail> {

    @Select("   SELECT" +
            "       d.*," +
            "       p.product_name,p.product_id product_id2 " +
            "   FROM" +
            "       `t_sale_order_detail` d" +
            "       LEFT JOIN t_product p ON ( p.product_id = d.product_id )" +
            "   WHERE d.master_id = #{masterId} " +
            "       and d.delete_status = 0")
    @Results({
            @Result(property = "product", column = "product_id2", one = @One(select = "com.qcmoke.oms.mapper.ProductMapper.selectById"))
    })
    List<SaleOrderDetailVo> getListByMasterId(@Param("masterId") Long masterId);
}
