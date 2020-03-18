package com.qcmoke.pms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.pms.dto.PurchaseOrderMasterDto;
import com.qcmoke.pms.entity.PurchaseOrderMaster;
import com.qcmoke.pms.service.PurchaseOrderMasterService;
import com.qcmoke.pms.vo.PurchaseOrderMasterVo;
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
@RestController
@RequestMapping("/purchaseOrderMaster")
public class PurchaseOrderMasterController {
    @Autowired
    private PurchaseOrderMasterService purchaseOrderMasterService;

    @GetMapping
    public Result<PageResult<PurchaseOrderMasterVo>> page(PageQuery pageQuery, PurchaseOrderMaster purchaseOrderMaster) {
        Page<PurchaseOrderMaster> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        PageResult<PurchaseOrderMasterVo> pageResult = purchaseOrderMasterService.getPage(page, purchaseOrderMaster);
        return Result.ok(pageResult);
    }

    @GetMapping("/pageForAddStock")
    public Result<PageResult<PurchaseOrderMasterVo>> pageForAddStock(PageQuery pageQuery, PurchaseOrderMaster purchaseOrderMaster) {
        Page<PurchaseOrderMaster> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        PageResult<PurchaseOrderMasterVo> pageResult = purchaseOrderMasterService.pageForAddStock(page, purchaseOrderMaster);
        return Result.ok(pageResult);
    }


    @GetMapping("/{id}")
    public Result<PurchaseOrderMaster> one(@PathVariable String id) {
        if (StringUtils.isBlank(id)) {
            throw new GlobalCommonException("id is required");
        }
        PurchaseOrderMaster materielVo = purchaseOrderMasterService.getById(id);
        return Result.ok(materielVo);
    }

    /**
     * 添加订单
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

    @DeleteMapping("/{ids}")
    public Result<Boolean> delete(@PathVariable String ids) {
        List<Long> idList = WebUtil.parseIdStrToLongList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            throw new GlobalCommonException("ids is required");
        }
        purchaseOrderMasterService.deleteByIdList(idList);
        return Result.ok("删除成功");
    }


    @PutMapping("/updateStatus")
    public Result<Boolean> updateStatus(PurchaseOrderMaster purchaseOrderMasterDto) {
        boolean flag = purchaseOrderMasterService.updateStatus(purchaseOrderMasterDto);
        return flag ? Result.ok() : Result.error();
    }


    @PutMapping("/transferToStock/{masterId}")
    public void transferToStock(@PathVariable Long masterId) {
        if (masterId == null) {
            throw new GlobalCommonException("masterId is required");
        }
        Long currentUserId = OauthSecurityJwtUtil.getCurrentUserId();
        purchaseOrderMasterService.transferToStock(masterId, currentUserId);
    }

}

