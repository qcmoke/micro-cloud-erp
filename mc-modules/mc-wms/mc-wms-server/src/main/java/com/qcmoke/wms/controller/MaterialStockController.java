package com.qcmoke.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.BeanCopyUtil;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.Result;
import com.qcmoke.wms.api.MaterialStockApi;
import com.qcmoke.wms.dto.StockItemDetailDto;
import com.qcmoke.wms.dto.StockItemDto;
import com.qcmoke.wms.entity.Stock;
import com.qcmoke.wms.entity.StockItem;
import com.qcmoke.wms.entity.StockItemDetail;
import com.qcmoke.wms.service.StockItemDetailService;
import com.qcmoke.wms.service.StockItemService;
import com.qcmoke.wms.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-12
 */
@Slf4j
@RestController
@RequestMapping("/materialStock")
public class MaterialStockController implements MaterialStockApi {
    @Autowired
    private StockService stockService;
    @Autowired
    private StockItemService stockItemService;
    @Autowired
    private StockItemDetailService stockItemDetailService;


    @RequestMapping(value = "/transferToStock", method = RequestMethod.POST)
    @Override
    public Result<?> transferToStock(@RequestBody StockItemDto stockItemDto) {
        if (stockItemDto == null || CollectionUtils.isEmpty(stockItemDto.getStockItemDetailDtoList())) {
            return Result.error();
        }
        Long currentUserId = OauthSecurityJwtUtil.getCurrentUserId();
        StockItem stockItem = BeanCopyUtil.copy(stockItemDto, StockItem.class);
        stockItem.setStockType(1);
        stockItem.setItemType(1);
        stockItem.setCheckStatus(1);
        stockItem.setFinishStatus(1);
        stockItem.setApplyUserId(currentUserId);
        stockItem.setCreateTime(new Date());
        boolean flag = stockItemService.save(stockItem);
        if (!flag) {
            return Result.error();
        }
        //生成出入库明细（移交明细）
        List<StockItemDetailDto> detailDtoList = stockItemDto.getStockItemDetailDtoList();
        List<StockItemDetail> detailList = BeanCopyUtil.copy(detailDtoList, StockItemDetail.class);
        if (detailList != null) {
            detailList.forEach(detail -> {
                detail.setStockItemId(stockItem.getStockItemId());
                detail.setCreateTime(new Date());
            });
            flag = stockItemDetailService.saveBatch(detailList);
        }
        return flag ? Result.ok() : Result.error();
    }


    @Transactional(rollbackFor = Exception.class)
    @PutMapping("/checkToPass")
    public Result<Boolean> checkToPass(List<Long> stockItemIds) {
        boolean flag = false;
        if (CollectionUtils.isNotEmpty(stockItemIds)) {
            flag = stockItemService.update(new LambdaQueryWrapper<StockItem>()
                    .eq(StockItem::getStockType, 1)
                    .eq(StockItem::getItemType, 1)
                    .in(StockItem::getStockItemId, stockItemIds));
        }
        return flag ? Result.ok("操作成功", true) : Result.error("操作失败", false);
    }


    /**
     * 物料入库
     */
    @PutMapping("/addItemToStock/{stockItemIds}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> addItemToStock(@PathVariable String stockItemIds) {
        List<Long> stockItemIdList = WebUtil.parseIdStrToLongList(stockItemIds);
        Long currentUserId = OauthSecurityJwtUtil.getCurrentUserId();
        //将相关的入库单，设置为已入库状态
        List<StockItem> stockItemList = stockItemService.list(new LambdaQueryWrapper<StockItem>()
                .eq(StockItem::getStockType, 1)
                .eq(StockItem::getItemType, 1)
                .in(StockItem::getStockItemId, stockItemIdList));

        if (CollectionUtils.isEmpty(stockItemList)) {
            throw new GlobalCommonException("不存在相关记录");
        }

        stockItemList.forEach(stockItem -> {
            if (stockItem.getCheckStatus() == null || stockItem.getCheckStatus() < 3) {
                throw new GlobalCommonException("存在未审核完成的入库申请");
            }
            if (stockItem.getFinishStatus() == 2) {
                throw new GlobalCommonException("存在已经完成入库的货物");
            }
            stockItem.setAdminId(currentUserId);
            stockItem.setCheckStatus(3);
            stockItem.setFinishStatus(2);
            stockItem.setModifyTime(new Date());
        });
        boolean flag = stockItemService.saveBatch(stockItemList);
        if (!flag) {
            throw new GlobalCommonException("入库失败,修改入库状态失败");
        }

        //计算相关入库单的明细，并计算库存量
        List<Stock> stockItems = new ArrayList<>();
        //获取相关明细
        List<StockItemDetail> detailList = stockItemDetailService.list(new LambdaQueryWrapper<StockItemDetail>().in(StockItemDetail::getStockItemId, stockItemIdList));
        if (CollectionUtils.isEmpty(detailList)) {
            throw new GlobalCommonException("不存在相关明细");
        }

        Set<Long> itemIdSet = detailList.stream().map(StockItemDetail::getItemId).collect(Collectors.toSet());
        List<Stock> finalExistedStockList = stockService.list(new LambdaQueryWrapper<Stock>().in(Stock::getItemId, itemIdSet));
        detailList.forEach(detail -> {
            Stock stock = new Stock();
            stock.setItemType(1);
            Long itemId = detail.getItemId();
            stock.setItemId(itemId);
            //已经存在的物料，则对其库存数进行累加（设置id，让其在saveOrUpdate中做修改）
            Stock existedStock = (Stock) CollectionUtils.find(finalExistedStockList, object -> itemId.equals(((Stock) object).getItemId()));
            if (existedStock != null) {
                stock.setStockId(existedStock.getStockId());
                stock.setItemCount(existedStock.getItemCount() + detail.getItemNum());
                stock.setModifyTime(new Date());
            } else { //不存在的则不设置id，让其为在saveOrUpdate中做新建
                stock.setCreateTime(new Date());
                stock.setItemCount(detail.getItemNum());
            }
            stockItems.add(stock);
        });
        //有id则修改，无id则新增
        flag = stockService.saveOrUpdateBatch(stockItems);
        return flag ? Result.ok("入库成功", true) : Result.error("入库失败", false);
    }

}