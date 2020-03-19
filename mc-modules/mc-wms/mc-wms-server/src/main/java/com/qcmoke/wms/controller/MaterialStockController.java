package com.qcmoke.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qcmoke.common.utils.BeanCopyUtil;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.Result;
import com.qcmoke.wms.api.MaterialStockApi;
import com.qcmoke.wms.dto.StockItemDetailDto;
import com.qcmoke.wms.dto.StockItemDto;
import com.qcmoke.wms.entity.StockItem;
import com.qcmoke.wms.entity.StockItemDetail;
import com.qcmoke.wms.service.StockItemDetailService;
import com.qcmoke.wms.service.StockItemService;
import com.qcmoke.wms.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
    @Transactional(rollbackFor = Exception.class)
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
    public Result<Object> addItemToStock(@PathVariable String stockItemIds) {
        /*检查初始化库存货物*/
        stockService.checkInitStock();
        stockService.addItemToStock(stockItemIds);
        return Result.ok("入库成功");
    }

}