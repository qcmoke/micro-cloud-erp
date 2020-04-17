package com.qcmoke.wms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.wms.dto.OutItemFromStockDto;
import com.qcmoke.wms.dto.StockQuery;
import com.qcmoke.wms.entity.Stock;
import com.qcmoke.wms.vo.StockVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-17
 */
public interface StockService extends IService<com.qcmoke.wms.entity.Stock> {

    void checkInitStock();

    void addItemToStock(List<Long> stockItemIdList);

    IPage<StockVo> getPage(Page<Stock> page, StockQuery stockDto);

    void outItemFromStock(OutItemFromStockDto outItemFromStockDto);
}
