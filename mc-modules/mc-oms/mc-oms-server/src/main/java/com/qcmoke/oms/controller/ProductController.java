package com.qcmoke.oms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.oms.entity.Product;
import com.qcmoke.oms.service.ProductService;
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
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public Result<List<Product>> getProductList() {
        List<Product> products = productService.list();
        return Result.ok(products);
    }


    @GetMapping
    public Result<PageResult<Product>> page(PageQuery pageQuery, Product product) {
        IPage<Product> pageInfo = productService.page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()), new QueryWrapper<>(product));
        PageResult<Product> pageResult = new PageResult<>(pageInfo.getRecords(), pageInfo.getTotal());
        return Result.ok(pageResult);
    }

    @GetMapping("/{id}")
    public Result<Product> one(@PathVariable String id) {
        if (StringUtils.isBlank(id)) {
            throw new GlobalCommonException("id is required");
        }
        Product product = productService.getById(id);
        return Result.ok(product);
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> add(Product product) {
        if (StringUtils.isBlank(product.getProductName())) {
            throw new GlobalCommonException("productName or unit is required");
        }
        product.setCreateTime(new Date());
        boolean save = productService.save(product);
        return Result.ok(save);
    }

    @DeleteMapping("/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delete(@PathVariable String ids) {
        List<Long> idList = WebUtil.parseIdStrToLongList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            throw new GlobalCommonException("ids is required");
        }
        boolean status = productService.removeByIds(idList);
        return status ? Result.ok() : Result.error();
    }


    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> update(Product product) {
        if (StringUtils.isBlank(product.getProductName())) {
            throw new GlobalCommonException("productName or unit is required");
        }
        product.setModifyTime(new Date());
        boolean flag = productService.updateById(product);
        return flag ? Result.ok() : Result.error();
    }

}

