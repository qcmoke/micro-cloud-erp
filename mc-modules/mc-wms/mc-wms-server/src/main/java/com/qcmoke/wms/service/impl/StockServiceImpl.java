package com.qcmoke.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.wms.entity.Stock;
import com.qcmoke.wms.entity.StockItem;
import com.qcmoke.wms.entity.StockItemDetail;
import com.qcmoke.wms.mapper.StockMapper;
import com.qcmoke.wms.service.StockItemDetailService;
import com.qcmoke.wms.service.StockItemService;
import com.qcmoke.wms.service.StockService;
import com.qcmoke.wms.vo.StockVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-17
 */
@Service
public class StockServiceImpl extends ServiceImpl<StockMapper, com.qcmoke.wms.entity.Stock> implements StockService {
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private StockItemDetailService stockItemDetailService;
    @Autowired
    private StockItemService stockItemService;

    @Override
    public IPage<StockVo> getPage(Page<Stock> page, Stock stockDto) {
        return stockMapper.getPage(page, stockDto);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addItemToStock(String stockItemIds) {
        List<Long> stockItemIdList = WebUtil.parseIdStrToLongList(stockItemIds);
        /*（1）将相关的入库单，设置为已入库状态*/
        Long currentUserId = OauthSecurityJwtUtil.getCurrentUserId();
        List<StockItem> stockItemList = stockItemService.list(new LambdaQueryWrapper<StockItem>()
                .eq(StockItem::getStockType, 1)
                .eq(StockItem::getItemType, 1)
                .in(StockItem::getStockItemId, stockItemIdList));
        if (CollectionUtils.isEmpty(stockItemList)) {
            throw new GlobalCommonException("不存在相关的订单记录");
        }
        stockItemList.forEach(stockItem -> {
            if (stockItem.getCheckStatus() == null || stockItem.getCheckStatus() < 3) {
                throw new GlobalCommonException("该订单存在未审核通过，请联系管理员审核");
            }
            if (stockItem.getFinishStatus() == 2) {
                throw new GlobalCommonException("该订单已经完成入库");
            }
            stockItem.setAdminId(currentUserId);
            stockItem.setFinishStatus(2);
            stockItem.setModifyTime(new Date());
        });
        boolean flag = stockItemService.updateBatchById(stockItemList);
        if (!flag) {
            throw new GlobalCommonException("修改入库状态失败");
        }

        /*（2）将明细对应的物料加入库存*/
        //计算相关入库单的明细，并计算库存量
        //获取相关明细
        List<StockItemDetail> detailList = stockItemDetailService.list(new LambdaQueryWrapper<StockItemDetail>().in(StockItemDetail::getStockItemId, stockItemIdList));
        if (CollectionUtils.isEmpty(detailList)) {
            throw new GlobalCommonException("不存在入库相关明细");
        }
        Set<Long> itemIdSet = detailList.stream().map(StockItemDetail::getItemId).collect(Collectors.toSet());
        List<com.qcmoke.wms.entity.Stock> existedStockList = this.list(new LambdaQueryWrapper<com.qcmoke.wms.entity.Stock>().in(com.qcmoke.wms.entity.Stock::getItemId, itemIdSet));
        List<com.qcmoke.wms.entity.Stock> updateStockItems = new ArrayList<>();
        detailList.forEach(detail -> {
            com.qcmoke.wms.entity.Stock stock = new com.qcmoke.wms.entity.Stock();
            stock.setItemType(1);
            Long itemId = detail.getItemId();
            stock.setItemId(itemId);
            //已经存在的物料，则对其库存数进行累加（设置id，让其在saveOrUpdate中做修改）
            com.qcmoke.wms.entity.Stock existedStock = (com.qcmoke.wms.entity.Stock) CollectionUtils.find(existedStockList, object -> itemId.equals(((com.qcmoke.wms.entity.Stock) object).getItemId()));
            if (existedStock == null) {
                throw new GlobalCommonException("存在未初始化的货物库存");
            }
            stock.setStockId(existedStock.getStockId());
            stock.setItemCount(existedStock.getItemCount() + detail.getItemNum());
            stock.setModifyTime(new Date());
            updateStockItems.add(stock);
        });
        flag = this.updateBatchById(updateStockItems);
        if (!flag) {
            throw new GlobalCommonException("入库失败");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkInitStock() {
        List<com.qcmoke.wms.entity.Stock> stockList = stockMapper.getAllMaterialsAndProductsNotInStock();
        if (CollectionUtils.isEmpty(stockList)) {
            return;
        }
        boolean flag = this.saveBatch(stockList);
        if (!flag) {
            throw new GlobalCommonException("库存货物初始化失败");
        }
    }

}
