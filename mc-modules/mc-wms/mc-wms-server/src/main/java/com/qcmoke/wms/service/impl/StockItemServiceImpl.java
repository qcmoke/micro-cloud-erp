package com.qcmoke.wms.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.Result;
import com.qcmoke.wms.entity.StockItem;
import com.qcmoke.wms.mapper.StockItemMapper;
import com.qcmoke.wms.service.StockItemService;
import com.qcmoke.wms.vo.StockItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-17
 */
@Service
public class StockItemServiceImpl extends ServiceImpl<StockItemMapper, StockItem> implements StockItemService {

    @Autowired
    private StockItemMapper stockItemMapper;


    @Override
    public IPage<StockItemVo> getPage(Page<StockItem> page, StockItem stockItemDto) {
        return stockItemMapper.getPage(page, stockItemDto);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<Boolean> updateStatus(Long stockItemId, int status) {
        StockItem stockItem = new StockItem();
        stockItem.setStockItemId(stockItemId);
        stockItem.setCheckStatus(status);
        stockItem.setModifyTime(new Date());
        stockItem.setAdminId(OauthSecurityJwtUtil.getCurrentUserId());
        stockItem.setFinishStatus(1);
        boolean flag = this.updateById(stockItem);
        return flag ? Result.ok() : Result.error();
    }
}
