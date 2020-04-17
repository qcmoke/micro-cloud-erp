package com.qcmoke.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.BeanCopyUtil;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.wms.api.StockItemApi;
import com.qcmoke.wms.constant.CheckStatusEnum;
import com.qcmoke.wms.constant.FinishStatusEnum;
import com.qcmoke.wms.constant.ItemType;
import com.qcmoke.wms.constant.StockType;
import com.qcmoke.wms.dto.OutItemFromStockDto;
import com.qcmoke.wms.dto.StockItemDetailDto;
import com.qcmoke.wms.dto.StockItemDto;
import com.qcmoke.wms.dto.StockItemQuery;
import com.qcmoke.wms.entity.StockItem;
import com.qcmoke.wms.entity.StockItemDetail;
import com.qcmoke.wms.service.StockItemDetailService;
import com.qcmoke.wms.service.StockItemService;
import com.qcmoke.wms.service.StockService;
import com.qcmoke.wms.vo.StockItemVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
 * @since 2020-03-17
 */
@RestController
@RequestMapping("/stockItem")
public class StockItemController implements StockItemApi {

    @Autowired
    private StockItemService stockItemService;
    @Autowired
    private StockService stockService;
    @Autowired
    private StockItemDetailService stockItemDetailService;


    @GetMapping("/page")
    public Result<PageResult<StockItemVo>> page(PageQuery pageQuery, StockItemQuery query) {
        Page<StockItem> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        IPage<StockItemVo> pageInfo = stockItemService.getPage(page, query);
        PageResult<StockItemVo> pageResult = new PageResult<>(pageInfo.getRecords(), pageInfo.getTotal());
        return Result.ok(pageResult);
    }


    @PutMapping("/checkPass")
    public Result<Boolean> checkPass(Long stockItemId) {
        stockItemService.checkPass(stockItemId);
        return Result.ok();
    }

    @PutMapping("/checkFail")
    public Result<Boolean> checkFail(Long stockItemId) {
        stockItemService.checkFail(stockItemId);
        return Result.ok();
    }


    /**
     * 生成库存出入单
     */
    @RequestMapping(value = "/createStockPreReview", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result<?> createStockPreReview(@RequestBody StockItemDto stockItemDto) {
        if (stockItemDto == null) {
            return Result.error("参数有误");
        }
        List<StockItemDetailDto> stockItemDetailDtoList = stockItemDto.getStockItemDetailDtoList();
        StockType stockType = stockItemDto.getStockType();
        ItemType itemType = stockItemDto.getItemType();
        Long orderId = stockItemDto.getDealId();
        if (CollectionUtils.isEmpty(stockItemDetailDtoList)
                || stockType == null
                || itemType == null
                || orderId == null) {
            return Result.error("参数有误");
        }

        StockItem dbStockItem = stockItemService.getOne(
                new LambdaQueryWrapper<StockItem>()
                        .eq(StockItem::getDealId, orderId)
                        .eq(StockItem::getItemType, itemType.value())
                        .eq(StockItem::getStockType,stockType.value()));
        if (dbStockItem != null) {
            return Result.error("库存出入单已存在！");
        }
        StockItem stockItem = BeanCopyUtil.copy(stockItemDto, StockItem.class);
        Long currentUserId = OauthSecurityJwtUtil.getCurrentUserId();
        stockItem.setStockType(stockType.value());
        stockItem.setItemType(itemType.value());
        stockItem.setCheckStatus(CheckStatusEnum.NO_REVIEWED.value());
        stockItem.setFinishStatus(FinishStatusEnum.NO_FINISHED.value());
        stockItem.setApplyUserId(currentUserId);
        stockItem.setCreateTime(new Date());
        boolean flag = stockItemService.save(stockItem);
        if (!flag) {
            return Result.error();
        }
        //生成出入库明细
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


    /**
     * 进货入库
     */
    @PutMapping("/addItemToStock/{stockItemIds}")
    public Result<Object> addItemToStock(@PathVariable String stockItemIds) {
        List<Long> stockItemIdList = WebUtil.parseIdStrToLongList(stockItemIds);
        if (CollectionUtils.isEmpty(stockItemIdList)) {
            throw new GlobalCommonException("stockItemIds are required");
        }
        /*检查初始化库存货物*/
        stockService.checkInitStock();
        stockService.addItemToStock(stockItemIdList);
        return Result.ok("入库成功");
    }

    /**
     * 发货出库
     */
    @PutMapping("/outItemFromStock")
    public Result<Object> outItemFromStock(@RequestBody OutItemFromStockDto outItemFromStockDto) {
        if (StringUtils.isBlank(outItemFromStockDto.getDeliveryChannel())
                || StringUtils.isBlank(outItemFromStockDto.getDeliverySn())
                || outItemFromStockDto.getStockItemId() == null) {
            throw new GlobalCommonException("deliveryChannel deliverySn  stockItemId are required");
        }
        stockService.outItemFromStock(outItemFromStockDto);
        return Result.ok("发货成功");
    }
}