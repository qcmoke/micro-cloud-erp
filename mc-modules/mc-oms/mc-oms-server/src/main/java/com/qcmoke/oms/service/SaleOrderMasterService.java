package com.qcmoke.oms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.oms.dto.ApplyForDeliveryDto;
import com.qcmoke.oms.dto.OrderMasterDto;
import com.qcmoke.oms.dto.SaleOrderMasterApiDto;
import com.qcmoke.oms.dto.SaleOrderMasterQuery;
import com.qcmoke.oms.entity.SaleOrderMaster;
import com.qcmoke.oms.vo.SaleOrderMasterVo;
import com.qcmoke.wms.constant.StockType;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-10
 */
public interface SaleOrderMasterService extends IService<SaleOrderMaster> {

    void createOrUpdateSaleOrder(OrderMasterDto orderMasterDto);

    IPage<SaleOrderMasterVo> getPage(Page<SaleOrderMaster> page, SaleOrderMasterQuery saleOrderMasterQuery);


    void deleteByIdList(List<Long> idList);

    void confirmUserReceipt(SaleOrderMaster orderMaster, Boolean isReceived);

    void applyForDelivery(ApplyForDeliveryDto applyForDeliveryDto);

    void successForOutItemFromStock(SaleOrderMasterApiDto orderMasterDto);

    void checkCallBackForCreateStockItem(StockType stockType, Long orderId, boolean isOk);
}
