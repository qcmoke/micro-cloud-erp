package com.qcmoke.oms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.BeanCopyUtil;
import com.qcmoke.common.utils.CheckVariableUtil;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.oms.api.SaleOrderMasterApi;
import com.qcmoke.oms.dto.*;
import com.qcmoke.oms.entity.SaleOrderMaster;
import com.qcmoke.oms.service.SaleOrderMasterService;
import com.qcmoke.oms.service.SaleRefundService;
import com.qcmoke.oms.vo.SaleOrderMasterVo;
import com.qcmoke.wms.constant.StockType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-10
 */
@Slf4j
@RestController
@RequestMapping("/saleOrderMaster")
public class SaleOrderMasterController implements SaleOrderMasterApi {
    @Autowired
    private SaleOrderMasterService saleOrderMasterService;

    @Autowired
    private SaleRefundService saleRefundService;


    /**
     * TODO
     * 订单分页
     */
    @GetMapping("/page")
    public Result<PageResult<SaleOrderMasterVo>> page(PageQuery pageQuery, SaleOrderMasterQuery saleOrderMasterQuery) {
        Page<SaleOrderMaster> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        IPage<SaleOrderMasterVo> iPage = saleOrderMasterService.getPage(page, saleOrderMasterQuery);
        PageResult<SaleOrderMasterVo> pageResult = new PageResult<>();
        pageResult.setRows(iPage.getRecords());
        pageResult.setTotal(iPage.getTotal());
        return Result.ok(pageResult);
    }


    /**
     * 批量刪除
     */
    @DeleteMapping("/{ids}")
    public Result<Boolean> delete(@PathVariable String ids) {
        List<Long> idList = WebUtil.parseIdStrToLongList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            throw new GlobalCommonException("ids is required");
        }
        saleOrderMasterService.deleteByIdList(idList);
        return Result.ok("删除成功");
    }


    /**
     * 创建或修改订单
     */
    @PostMapping("/createOrUpdateSaleOrder")
    public Result<?> createOrUpdateSaleOrder(@RequestBody OrderMasterDto orderMasterDto) {
        saleOrderMasterService.createOrUpdateSaleOrder(orderMasterDto);
        return Result.ok();
    }


    /**
     * 提交发货申请单
     */
    @PutMapping("/applyForDeliver")
    public Result<?> applyForDelivery(@RequestBody ApplyForDeliveryDto applyForDeliveryDto) {
        if (applyForDeliveryDto == null) {
            throw new GlobalCommonException("applyForDeliveryDto is null");
        }
        if (applyForDeliveryDto.getMasterId() == null) {
            throw new GlobalCommonException("need some params !");
        }
        saleOrderMasterService.applyForDelivery(applyForDeliveryDto);
        return Result.ok();
    }


    /**
     * 收货确认处理
     */
    @PutMapping("/updateUserDelivery")
    public Result<?> confirmUserReceipt(@RequestBody UpdateDeliveryDto updateDeliveryDto) {
        if (updateDeliveryDto == null) {
            throw new GlobalCommonException("illegal");
        }
        Long masterId = updateDeliveryDto.getMasterId();
        String receiverName = updateDeliveryDto.getReceiverName();
        String receiverDetailAddress = updateDeliveryDto.getReceiverDetailAddress();
        String receiverPhone = updateDeliveryDto.getReceiverPhone();
        Boolean isReceived = updateDeliveryDto.getIsReceived();
        if (masterId == null
                || isReceived == null
                || StringUtils.isBlank(receiverName)
                || StringUtils.isBlank(receiverDetailAddress)
                || StringUtils.isBlank(receiverPhone)) {
            throw new GlobalCommonException("illegal");
        }
        if (!CheckVariableUtil.isMobile(receiverPhone)) {
            throw new GlobalCommonException("receiverPhone illegal");
        }
        SaleOrderMaster orderMaster = BeanCopyUtil.copy(updateDeliveryDto, SaleOrderMaster.class);
        saleOrderMasterService.confirmUserReceipt(orderMaster, isReceived);
        return Result.ok();
    }

    /**
     * 退货入库成功回调
     */
    @RequestMapping("/successForInItemToStock")
    @Override
    public Result<?> successForInItemToStock(@RequestBody List<Long> orderList) {
        log.info("入库成功回调,orderList={}", orderList);
        if (CollectionUtils.isEmpty(orderList)) {
            throw new GlobalCommonException("orderList is required");
        }
        saleRefundService.successForInItemToStock(orderList);
        return Result.ok();
    }


    /**
     * 审核结果回调
     */
    @Override
    @RequestMapping(value = "/checkCallBackForCreateStockItem", method = RequestMethod.GET)
    public Result<?> checkCallBackForCreateStockItem(@RequestParam("stockType") StockType stockType, @RequestParam("orderId") Long orderId, @RequestParam("isOk") boolean isOk) {
        log.info("审核结果回调,stockType={},orderId={},isOk={}", stockType, orderId, isOk);
        if (orderId == null || stockType == null) {
            throw new GlobalCommonException("orderId is required");
        }
        saleOrderMasterService.checkCallBackForCreateStockItem(stockType, orderId, isOk);
        return Result.ok();
    }


    /**
     * 出库成功回调
     */
    @RequestMapping("/successForOutItemFromStock")
    @Override
    public Result<?> successForOutItemFromStock(@RequestBody SaleOrderMasterApiDto saleOrderMasterApiDto) {
        log.info("出库成功回调,saleOrderMasterApiDto={}", saleOrderMasterApiDto);
        if (StringUtils.isBlank(saleOrderMasterApiDto.getDeliveryChannel())
                || StringUtils.isBlank(saleOrderMasterApiDto.getDeliverySn())
                || saleOrderMasterApiDto.getMasterId() == null) {
            throw new GlobalCommonException("deliveryChannel deliverySn  masterId are required");
        }
        saleOrderMasterService.successForOutItemFromStock(saleOrderMasterApiDto);
        return Result.ok();
    }

}