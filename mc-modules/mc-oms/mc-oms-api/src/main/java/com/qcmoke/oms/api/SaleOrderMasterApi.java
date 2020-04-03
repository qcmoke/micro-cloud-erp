package com.qcmoke.oms.api;

import com.qcmoke.common.vo.Result;
import com.qcmoke.oms.dto.SaleOrderMasterApiDto;
import com.qcmoke.wms.constant.StockType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author qcmoke
 */
@RequestMapping("/saleOrderMaster")
public interface SaleOrderMasterApi {

    /**
     * 入库成功回调
     */
    @RequestMapping("/successForInItemToStock")
    Result<?> successForInItemToStock(@RequestBody List<Long> orderList);

    /**
     * 出库成功回调
     */
    @RequestMapping("/successForOutItemFromStock")
    Result<?> successForOutItemFromStock(@RequestBody SaleOrderMasterApiDto orderMasterDto);

    /**
     * 审核结果回调
     */
    @RequestMapping("/checkCallBackForCreateStockItem")
    Result<?> checkCallBackForCreateStockItem(@RequestParam("stockType") StockType stockType, @RequestParam("orderId") Long orderId, @RequestParam("isOk") boolean isOk);

}
