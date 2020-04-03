package com.qcmoke.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.oms.entity.SaleRefund;
import com.qcmoke.oms.vo.SaleRefundVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-10
 */
public interface SaleRefundService extends IService<SaleRefund> {

    void saleRefund(SaleRefund saleRefund);

    void successForInItemToStock(List<Long> orderList);

    IPage<SaleRefundVo> getPage(Page<SaleRefund> page, SaleRefund saleRefund);
}
