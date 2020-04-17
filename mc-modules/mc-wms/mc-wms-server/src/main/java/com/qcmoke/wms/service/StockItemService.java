package com.qcmoke.wms.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.wms.dto.StockItemQuery;
import com.qcmoke.wms.entity.StockItem;
import com.qcmoke.wms.vo.StockItemVo;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-17
 */
public interface StockItemService extends IService<StockItem> {
    IPage<StockItemVo> getPage(Page<StockItem> page, StockItemQuery query);


    void checkPass(Long stockItemId);

    void checkFail(Long stockItemId);
}
