package com.qcmoke.wms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.wms.entity.Stock;
import com.qcmoke.wms.vo.StockVo;

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

    void addItemToStock(String stockItemIds);

    IPage<StockVo> getPage(Page<Stock> page, Stock stockDto);

}
