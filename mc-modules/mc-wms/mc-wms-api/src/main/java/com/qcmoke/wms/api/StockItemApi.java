package com.qcmoke.wms.api;


import com.qcmoke.common.vo.Result;
import com.qcmoke.wms.dto.StockItemDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author qcmoke
 * @since 2020-03-12
 */
@RequestMapping("/stockItem")
public interface StockItemApi {

    /**
     * 生成库存出入预审核表
     *
     * @param stockItemDto
     * @return
     */
    @RequestMapping(value = "/createStockPreReview", method = RequestMethod.POST)
    Result<?> createStockPreReview(@RequestBody StockItemDto stockItemDto);
}