package com.qcmoke.oms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.oms.entity.Customer;
import com.qcmoke.oms.service.CustomerService;
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
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    /**
     * 获取用户列表
     *
     */
    @GetMapping("/list")
    public Result<List<Customer>> getCustomerList() {
        List<Customer> customers = customerService.list();
        return Result.ok(customers);
    }


    @GetMapping
    public Result<PageResult<Customer>> page(PageQuery pageQuery, Customer customer) {
        IPage<Customer> pageInfo = customerService.page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()), new QueryWrapper<>(customer));
        PageResult<Customer> pageResult = new PageResult<>(pageInfo.getRecords(), pageInfo.getTotal());
        return Result.ok(pageResult);
    }

    @GetMapping("/{id}")
    public Result<Customer> one(@PathVariable String id) {
        if (StringUtils.isBlank(id)) {
            throw new GlobalCommonException("id is required");
        }
        Customer customer = customerService.getById(id);
        return Result.ok(customer);
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> add(Customer customer) {
        if (StringUtils.isBlank(customer.getCustomerName())) {
            throw new GlobalCommonException("customerName or unit is required");
        }
        customer.setCreateTime(new Date());
        boolean save = customerService.save(customer);
        return Result.ok(save);
    }

    @DeleteMapping("/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delete(@PathVariable String ids) {
        List<Long> idList = WebUtil.parseIdStrToLongList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            throw new GlobalCommonException("materialIds is required");
        }
        boolean status = customerService.removeByIds(idList);
        return status ? Result.ok() : Result.error();
    }


    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> update(Customer customer) {
        if (StringUtils.isBlank(customer.getCustomerName())) {
            throw new GlobalCommonException("customerName or unit is required");
        }
        customer.setModifyTime(new Date());
        boolean flag = customerService.updateById(customer);
        return flag ? Result.ok() : Result.error();
    }

}

