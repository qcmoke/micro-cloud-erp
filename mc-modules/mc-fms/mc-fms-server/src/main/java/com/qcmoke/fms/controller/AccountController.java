package com.qcmoke.fms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.fms.entity.Account;
import com.qcmoke.fms.service.AccountService;
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
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/page")
    public Result<PageResult<Account>> page(PageQuery pageQuery, Account account) {
        IPage<Account> pageInfo = accountService.page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()), new QueryWrapper<>(account));
        PageResult<Account> pageResult = new PageResult<>(pageInfo.getRecords(), pageInfo.getTotal());
        return Result.ok(pageResult);
    }


    @PostMapping("/saveOrUpdate")
    public Result<Boolean> saveOrUpdate(@RequestBody Account account) {
        if (StringUtils.isBlank(account.getAccountName())
                || StringUtils.isBlank(account.getBankName())
                || StringUtils.isBlank(account.getBankName())
                || account.getAmount() == null
                || account.getIsDefault() == null
                || account.getBankNum() == null) {
            throw new GlobalCommonException("some params are required");
        }
        account.setCreateTime(new Date());
        boolean save = accountService.saveOrUpdate(account);
        return Result.ok(save);
    }


    @DeleteMapping("/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delete(@PathVariable String ids) {
        List<Long> idList = WebUtil.parseIdStrToLongList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            throw new GlobalCommonException("ids is required");
        }
        boolean status = accountService.removeByIds(idList);
        return status ? Result.ok() : Result.error();
    }


}

