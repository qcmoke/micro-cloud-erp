package com.qcmoke.oms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.oms.entity.SaleRefund;
import com.qcmoke.oms.service.SaleRefundService;
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
 * @since 2020-03-10
 */
@RestController
@RequestMapping("/saleRefund")
public class SaleRefundController {

    @Autowired
    private SaleRefundService saleRefundService;


    @GetMapping
    public Result<PageResult<SaleRefund>> page(PageQuery pageQuery, SaleRefund saleRefund) {
        IPage<SaleRefund> pageInfo = saleRefundService.page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()), new QueryWrapper<>(saleRefund));
        PageResult<SaleRefund> pageResult = new PageResult<>(pageInfo.getRecords(), pageInfo.getTotal());
        return Result.ok(pageResult);
    }

    @GetMapping("/{id}")
    public Result<SaleRefund> one(@PathVariable String id) {
        if (StringUtils.isBlank(id)) {
            throw new GlobalCommonException("id is required");
        }
        SaleRefund saleRefund = saleRefundService.getById(id);
        return Result.ok(saleRefund);
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> add(@RequestBody SaleRefund saleRefund) {
        if (saleRefund.getSaleOrderMasterId() == null || saleRefund.getTotalAmount() == null) {
            throw new GlobalCommonException("some params are required");
        }
        saleRefund.setCreateTime(new Date());
        boolean save = saleRefundService.save(saleRefund);
        return Result.ok(save);
    }

    @DeleteMapping("/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delete(@PathVariable String ids) {
        List<Long> idList = WebUtil.parseIdStrToLongList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            throw new GlobalCommonException("ids is required");
        }
        boolean status = saleRefundService.removeByIds(idList);
        return status ? Result.ok() : Result.error();
    }


    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> update(SaleRefund saleRefund) {
        if (saleRefund.getRefundId() == null || saleRefund.getTotalAmount() == null) {
            throw new GlobalCommonException("some params are required");
        }
        saleRefund.setModifyTime(new Date());
        boolean flag = saleRefundService.updateById(saleRefund);
        return flag ? Result.ok() : Result.error();
    }
}

