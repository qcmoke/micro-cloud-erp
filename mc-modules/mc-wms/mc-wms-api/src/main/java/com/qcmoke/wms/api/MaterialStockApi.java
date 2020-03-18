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
@RequestMapping("/materialStock")
public interface MaterialStockApi {

    @RequestMapping(value = "/transferToStock", method = RequestMethod.POST)
    Result<?> transferToStock(@RequestBody StockItemDto stockItemDto);
}