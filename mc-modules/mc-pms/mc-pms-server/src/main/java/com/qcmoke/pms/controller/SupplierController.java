package com.qcmoke.pms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.pms.entity.Supplier;
import com.qcmoke.pms.service.SupplierService;
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
 * @since 2020-03-12
 */
@RestController
@RequestMapping("/supplier")
public class SupplierController {


    @Autowired
    private SupplierService supplierService;


    @GetMapping("/list")
    public Result<List<Supplier>> getAllSuppliers() {
        return Result.ok(supplierService.list());
    }

    @GetMapping
    public Result<PageResult<Supplier>> page(PageQuery pageQuery, Supplier materialDto) {
        IPage<Supplier> pageInfo = supplierService.page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()), new QueryWrapper<>(materialDto));
        PageResult<Supplier> pageResult = new PageResult<>(pageInfo.getRecords(), pageInfo.getTotal());
        return Result.ok(pageResult);
    }

    @GetMapping("/{id}")
    public Result<Supplier> one(@PathVariable String id) {
        if (StringUtils.isBlank(id)) {
            throw new GlobalCommonException("id is required");
        }
        Supplier materielVo = supplierService.getById(id);
        return Result.ok(materielVo);
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> add(Supplier supplier) {
        supplier.setCreateTime(new Date());
        boolean save = supplierService.save(supplier);
        return Result.ok(save);
    }

    @DeleteMapping("/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delete(@PathVariable String ids) {
        List<String> idList = WebUtil.parseIdStr2List(ids);
        if (CollectionUtils.isEmpty(idList)) {
            throw new GlobalCommonException("ids is required");
        }
        boolean status = supplierService.removeByIds(idList);
        return status ? Result.ok() : Result.error();
    }


    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> update(Supplier material) {
        material.setModifyTime(new Date());
        boolean flag = supplierService.updateById(material);
        return flag ? Result.ok() : Result.error();
    }

}

