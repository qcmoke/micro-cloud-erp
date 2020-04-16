package com.qcmoke.pms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.pms.api.PurchaseOrderMasterApi;
import com.qcmoke.pms.dto.PurchaseOrderMasterApiDto;
import com.qcmoke.pms.dto.PurchaseOrderMasterDto;
import com.qcmoke.pms.dto.PurchaseOrderMasterQuery;
import com.qcmoke.pms.entity.PurchaseOrderMaster;
import com.qcmoke.pms.service.PurchaseOrderMasterService;
import com.qcmoke.pms.vo.PurchaseOrderMasterVo;
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
 * @since 2020-03-12
 */
@Slf4j
@RestController
@RequestMapping("/purchaseOrderMaster")
public class PurchaseOrderMasterController implements PurchaseOrderMasterApi {
    @Autowired
    private PurchaseOrderMasterService purchaseOrderMasterService;

    /**
     * 分页查询
     */
    @GetMapping
    public Result<PageResult<PurchaseOrderMasterVo>> page(PageQuery pageQuery, PurchaseOrderMasterQuery purchaseOrderMasterQuery) {
        Page<PurchaseOrderMaster> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        PageResult<PurchaseOrderMasterVo> pageResult = purchaseOrderMasterService.getPage(page, purchaseOrderMasterQuery);
        return Result.ok(pageResult);
    }


    /**
     * 通过id查询
     */
    @GetMapping("/{id}")
    public Result<PurchaseOrderMaster> getById(@PathVariable String id) {
        if (StringUtils.isBlank(id)) {
            throw new GlobalCommonException("id is required");
        }
        PurchaseOrderMaster materielVo = purchaseOrderMasterService.getById(id);
        return Result.ok(materielVo);
    }

    /**
     * 创建或修改采购订单
     */
    @PostMapping("/createOrUpdatePurchaseOrder")
    public Result<Boolean> createOrUpdatePurchaseOrder(@RequestBody PurchaseOrderMasterDto purchaseOrderMasterDto) {
        Long currentUserId = OauthSecurityJwtUtil.getCurrentUserId();
        if (purchaseOrderMasterDto == null || CollectionUtils.isEmpty(purchaseOrderMasterDto.getPurchaseOrderDetailList())) {
            throw new GlobalCommonException("没有有效的修改请求数据");
        }
        purchaseOrderMasterService.createOrUpdatePurchaseOrder(purchaseOrderMasterDto, currentUserId);
        return Result.ok("操作成功");
    }

    /**
     * 通过id批量删除
     */
    @DeleteMapping("/{ids}")
    public Result<Boolean> delete(@PathVariable String ids) {
        List<Long> idList = WebUtil.parseIdStrToLongList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            throw new GlobalCommonException("ids is required");
        }
        purchaseOrderMasterService.deleteByIdList(idList);
        return Result.ok("删除成功");
    }


    /**
     * 提交审核申请
     */
    @PutMapping("/toApplyCheck/{masterId}")
    public Result<Boolean> toApplyCheck(@PathVariable Long masterId) {
        boolean flag = purchaseOrderMasterService.toApplyCheck(masterId);
        return flag ? Result.ok() : Result.error();
    }

    /**
     * 审核通过
     */
    @PutMapping("/checkPass/{masterId}")
    public Result<Boolean> checkPass(@PathVariable Long masterId) {
        boolean flag = purchaseOrderMasterService.checkPass(masterId);
        return flag ? Result.ok() : Result.error();
    }

    /**
     * 审核不通过
     */
    @PutMapping("/checkFail/{masterId}")
    public Result<Boolean> checkFail(@PathVariable Long masterId) {
        boolean flag = purchaseOrderMasterService.checkFail(masterId);
        return flag ? Result.ok() : Result.error();
    }


    /**
     * 生成入库单
     */
    @PutMapping("/applyToStock/{masterId}")
    public void applyToStock(@PathVariable Long masterId) {
        if (masterId == null) {
            throw new GlobalCommonException("masterId is required");
        }
        Long currentUserId = OauthSecurityJwtUtil.getCurrentUserId();
        purchaseOrderMasterService.applyToStock(masterId, currentUserId);
    }


    /**
     * 采购退货出库成功回调
     */
    @RequestMapping("/successForOutItemFromStock")
    @Override
    public Result<?> successForOutItemFromStock(@RequestBody PurchaseOrderMasterApiDto purchaseOrderMasterApiDto) {
        log.info("出库成功回调,purchaseOrderMasterApiDto={}", purchaseOrderMasterApiDto);
        purchaseOrderMasterService.successForOutItemFromStock(purchaseOrderMasterApiDto);
        return Result.ok();
    }

    /**
     * 采购入库成功回调
     */
    @Override
    @RequestMapping("/successForInItemToStock")
    public Result<?> successForInItemToStock(@RequestBody List<Long> masterIdList) {
        log.info("入库成功回调,masterIdList={}", masterIdList);
        if (CollectionUtils.isEmpty(masterIdList)) {
            throw new GlobalCommonException("masterIdList is required");
        }
        purchaseOrderMasterService.successForInItemToStock(masterIdList);
        return Result.ok();
    }

    /**
     * 审核结果回调
     */
    @Override
    @RequestMapping("/checkCallBackForCreateStockItem")
    public Result<?> checkCallBackForCreateStockItem(@RequestParam("stockType") StockType stockType, @RequestParam("orderId") Long orderId, @RequestParam("isOk") boolean isOk) {
        log.info("审核结果回调,stockType={},orderId={},isOk={}", stockType, orderId, isOk);
        if (orderId == null || stockType == null) {
            throw new GlobalCommonException("orderId is required");
        }
        purchaseOrderMasterService.checkCallBackForCreateStockItem(stockType, orderId, isOk);
        return Result.ok();
    }

}

