package com.qcmoke.pms.api;

import com.qcmoke.common.vo.Result;
import com.qcmoke.pms.dto.PurchaseOrderMasterApiDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author qcmoke
 */
@RequestMapping("/purchaseOrderMaster")
public interface PurchaseOrderMasterApi {
    /**
     * 入库成功回调
     */
    @RequestMapping("/successForInItemToStock")
    Result<?> successForInItemToStock(@RequestBody List<Long> masterIdList);

    /**
     * 出库成功回调
     */
    @RequestMapping("/successForOutItemFromStock")
    Result<?> successForOutItemFromStock(@RequestBody PurchaseOrderMasterApiDto purchaseOrderMasterApiDto);

    /**
     * 审核结果回调
     */
    @RequestMapping("/checkCallBackForCreateStockPreReview")
    Result<?> checkCallBackForCreateStockPreReview(@RequestParam("orderId") Long orderId, @RequestParam("isOk") boolean isOk);


}