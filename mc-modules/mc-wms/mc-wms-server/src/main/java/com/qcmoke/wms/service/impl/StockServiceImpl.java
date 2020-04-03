package com.qcmoke.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.Result;
import com.qcmoke.oms.dto.SaleOrderMasterApiDto;
import com.qcmoke.pms.dto.PurchaseOrderMasterApiDto;
import com.qcmoke.wms.client.PurchaseOrderMasterClient;
import com.qcmoke.wms.client.SaleOrderMasterClient;
import com.qcmoke.wms.constant.CheckStatusEnum;
import com.qcmoke.wms.constant.FinishStatusEnum;
import com.qcmoke.wms.constant.StockType;
import com.qcmoke.wms.dto.OutItemFromStockDto;
import com.qcmoke.wms.entity.Stock;
import com.qcmoke.wms.entity.StockItem;
import com.qcmoke.wms.entity.StockItemDetail;
import com.qcmoke.wms.mapper.StockMapper;
import com.qcmoke.wms.service.StockItemDetailService;
import com.qcmoke.wms.service.StockItemService;
import com.qcmoke.wms.service.StockService;
import com.qcmoke.wms.vo.StockVo;
import io.seata.spring.annotation.GlobalTransactional;
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
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private StockItemDetailService stockItemDetailService;
    @Autowired
    private StockItemService stockItemService;
    @Autowired
    private StockService stockService;
    @Autowired
    private SaleOrderMasterClient saleOrderMasterClient;
    @Autowired
    private PurchaseOrderMasterClient purchaseOrderMasterClient;


    @Override
    public IPage<StockVo> getPage(Page<Stock> page, Stock stockDto) {
        return stockMapper.getPage(page, stockDto);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void checkInitStock() {
        List<Stock> stockList = stockMapper.getAllMaterialsAndProductsNotInStock();
        if (CollectionUtils.isEmpty(stockList)) {
            return;
        }
        boolean flag = this.saveBatch(stockList);
        if (!flag) {
            throw new GlobalCommonException("库存货物初始化失败");
        }
    }


    /**
     * 发货出库
     * 步骤
     * 1、扣减相应货物的库存量
     * 2、设置发货申请单状态为已完成
     * 3、通知申请发库的服务修改其状态为发库成功
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void outItemFromStock(OutItemFromStockDto outItemFromStockDto) {
        Long stockItemId = outItemFromStockDto.getStockItemId();
        StockItem stockItem = stockItemService.getById(stockItemId);
        if (stockItem == null) {
            throw new GlobalCommonException("不存在相关记录");
        }
        /*1、扣减相应货物的库存量*/
        List<StockItemDetail> detailList = stockItemDetailService.list(new LambdaQueryWrapper<StockItemDetail>()
                .eq(StockItemDetail::getStockItemId, stockItemId));
        if (CollectionUtils.isEmpty(detailList)) {
            throw new GlobalCommonException("不存在相关明细");
        }
        Integer itemType = stockItem.getItemType();
        Set<Long> itemIdSet = detailList.stream().map(StockItemDetail::getItemId).collect(Collectors.toSet());
        List<Stock> stockList = this.list(new LambdaQueryWrapper<Stock>()
                .eq(Stock::getItemType, itemType)
                .in(Stock::getItemId, itemIdSet));
        if (CollectionUtils.isEmpty(stockList)) {
            throw new GlobalCommonException("不存在相关库存货物");
        }
        detailList.forEach(detail -> {
            Long itemId = detail.getItemId();
            Double itemNum = detail.getItemNum();
            Stock stock = (Stock) CollectionUtils.find(stockList, object -> ((Stock) object).getItemId().equals(itemId));
            Double itemCount = stock.getItemCount();
            if (itemCount <= 0) {
                throw new GlobalCommonException("库存量已经不足");
            }
            stock.setItemCount(itemCount - itemNum);
            stock.setModifyTime(new Date());
        });
        boolean flag = stockService.updateBatchById(stockList);
        if (!flag) {
            throw new GlobalCommonException("扣减库存失败！");
        }

        /*2、设置发货申请单状态为已完成*/
        if (stockItem.getCheckStatus() == null || stockItem.getCheckStatus() < CheckStatusEnum.PASS.value()) {
            throw new GlobalCommonException("该记录存在未审核通过，请联系管理员审核");
        }
        if (stockItem.getFinishStatus() == FinishStatusEnum.FINISHED.value()) {
            throw new GlobalCommonException("存在已经完成出库的订单");
        }
        stockItem.setAdminId(OauthSecurityJwtUtil.getCurrentUserId());
        stockItem.setFinishStatus(FinishStatusEnum.FINISHED.value());
        stockItem.setMakeDate(new Date());
        stockItem.setModifyTime(new Date());
        flag = stockItemService.updateById(stockItem);
        if (!flag) {
            throw new GlobalCommonException("修改状态失败");
        }

        /*3、通知申请发库的服务修改其状态为发库成功*/
        String deliveryChannel = outItemFromStockDto.getDeliveryChannel();
        String deliverySn = outItemFromStockDto.getDeliverySn();
        Long orderId = stockItem.getDealId();
        StockType stockType = StockType.valueOf(stockItem.getStockType());
        switch (stockType) {
            case SALE_OUT:
                Result<?> result1 = saleOrderMasterClient.successForOutItemFromStock(new SaleOrderMasterApiDto()
                        .setDeliveryChannel(deliveryChannel)
                        .setMasterId(orderId)
                        .setDeliverySn(deliverySn));
                if (result1.isError()) {
                    throw new GlobalCommonException("通知oms失败");
                }
                break;
            case PURCHASE_OUT:
                Result<?> result2 = purchaseOrderMasterClient.successForOutItemFromStock(new PurchaseOrderMasterApiDto()
                        .setDeliveryChannel(deliveryChannel)
                        .setMasterId(orderId)
                        .setDeliverySn(deliverySn));
                if (result2.isError()) {
                    throw new GlobalCommonException("通知pms失败");
                }
                break;
            default:
                throw new GlobalCommonException("暂不支持此操作！");
        }
    }


    /**
     * 进货入库
     * （1）将相关的入库单，设置为已入库状态
     * （2）计算明细，并将对应的物料加入库存
     * （3）通知申请入库的服务表明“入库成功”
     */
    @GlobalTransactional(rollbackFor = Exception.class)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addItemToStock(List<Long> stockItemIdList) {
        /*（1）将相关的入库单，设置为已入库状态*/
        Long currentUserId = OauthSecurityJwtUtil.getCurrentUserId();
        List<StockItem> stockItemList = stockItemService.listByIds(stockItemIdList);
        if (CollectionUtils.isEmpty(stockItemList)) {
            throw new GlobalCommonException("不存在相关的订单记录");
        }
        Integer bothItemType = stockItemList.get(0).getItemType();
        Integer bothStockType = stockItemList.get(0).getStockType();
        for (StockItem stockItem : stockItemList) {
            if (!bothItemType.equals(stockItem.getItemType())) {
                throw new GlobalCommonException("该操作只能同时存一种货物(物料或者产品)！");
            }
            if (!bothStockType.equals(stockItem.getStockType())) {
                throw new GlobalCommonException("该操作只能同时操作一种出入库类型)！");
            }
            if (stockItem.getCheckStatus() == null || stockItem.getCheckStatus() < CheckStatusEnum.PASS.value()) {
                throw new GlobalCommonException("存在未审核通过的订单");
            }
            if (stockItem.getFinishStatus() == FinishStatusEnum.FINISHED.value()) {
                throw new GlobalCommonException("存在已经完成入库的订单");
            }
            stockItem.setAdminId(currentUserId);
            stockItem.setMakeDate(new Date());
            stockItem.setFinishStatus(FinishStatusEnum.FINISHED.value());
            stockItem.setModifyTime(new Date());
        }
        boolean flag = stockItemService.updateBatchById(stockItemList);
        if (!flag) {
            throw new GlobalCommonException("修改入库状态失败");
        }

        /*（2）计算明细，并将对应的物料加入库存*/
        List<StockItemDetail> detailList = stockItemDetailService.list(new LambdaQueryWrapper<StockItemDetail>().in(StockItemDetail::getStockItemId, stockItemIdList));
        if (CollectionUtils.isEmpty(detailList)) {
            throw new GlobalCommonException("不存在入库相关明细");
        }
        Set<Long> itemIdSet = detailList.stream().map(StockItemDetail::getItemId).collect(Collectors.toSet());
        List<Stock> existedStockList = this.list(new LambdaQueryWrapper<Stock>()
                .eq(Stock::getItemType, bothItemType)
                .in(Stock::getItemId, itemIdSet));
        if (CollectionUtils.isEmpty(existedStockList)) {
            throw new GlobalCommonException("存在未初始化的货物库存");
        }
        List<Stock> updateStockItems = new ArrayList<>();
        detailList.forEach(detail -> {
            Stock stock = new Stock();
            stock.setItemType(bothItemType);
            Long itemId = detail.getItemId();
            stock.setItemId(itemId);
            //已经存在的物料，则对其库存数进行累加（设置id，让其在saveOrUpdate中做修改）
            Stock existedStock = (Stock) CollectionUtils.find(existedStockList, object -> itemId.equals(((Stock) object).getItemId()));
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

        //（3）通知申请入库的服务表明“入库成功”
        List<Long> orderList = stockItemList.stream().map(StockItem::getDealId).collect(Collectors.toList());
        StockType stockType = StockType.valueOf(bothStockType);
        switch (stockType) {
            case SALE_IN:
                Result<?> result = saleOrderMasterClient.successForInItemToStock(orderList);
                if (result.isError()) {
                    throw new GlobalCommonException("通知oms失败,e=" + result.getMessage());
                }
                break;
            case PURCHASE_IN:
                result = purchaseOrderMasterClient.successForInItemToStock(orderList);
                if (result.isError()) {
                    throw new GlobalCommonException("通知pms失败,e=" + result.getMessage());
                }
                break;
            default:
                throw new GlobalCommonException("暂不支持此操作！");
        }

    }
}
