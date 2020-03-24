package com.qcmoke.fms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.fms.api.BillApi;
import com.qcmoke.fms.constant.DealType;
import com.qcmoke.fms.constant.PayType;
import com.qcmoke.fms.dto.BillApiDto;
import com.qcmoke.fms.entity.Bill;
import com.qcmoke.fms.service.BillService;
import com.qcmoke.fms.vo.BillVo;
import com.qcmoke.fms.vo.StatisticsDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-10
 */
@RestController
@RequestMapping("/bill")
public class BillController implements BillApi {

    @Autowired
    private BillService billService;


    @Transactional(rollbackFor = Exception.class)
    @Override
    @RequestMapping(value = "/addBill", method = RequestMethod.POST)
    public Result<?> addBill(@RequestBody BillApiDto billApiDto) {
        Double totalAmount = billApiDto.getTotalAmount();
        Long dealNum = billApiDto.getDealNum();
        PayType payType = billApiDto.getPayType();
        DealType dealType = billApiDto.getDealType();
        int dealTypeValue = dealType.value();

        int count = billService.count(new LambdaQueryWrapper<Bill>()
                .eq(Bill::getDealNum, dealNum)
                .eq(Bill::getType, dealTypeValue));
        if (count > 0) {
            throw new GlobalCommonException("该账单已经存在!");
        }

        Bill bill = new Bill();
        bill.setDealNum(dealNum);
        bill.setTotalAmount(totalAmount);
        bill.setAccountId(Integer.toUnsignedLong(payType.value()));
        bill.setType(dealTypeValue);
        bill.setCreateTime(new Date());
        boolean save = billService.save(bill);
        if (!save) {
            throw new GlobalCommonException("创建账单失败!");
        }
        return Result.ok();
    }


    @GetMapping("/statistics/{year}")
    public Result<StatisticsDataVo> statistics(@PathVariable Integer year) {
        if (year == null) {
            throw new GlobalCommonException("year is required!");
        }
        return billService.statistics(year);
    }


    @GetMapping("/page")
    public Result<PageResult<BillVo>> page(PageQuery pageQuery, Bill bill) {
        IPage<BillVo> pageInfo = billService.getPage(new Page<Bill>(pageQuery.getPageNum(), pageQuery.getPageSize()), bill);
        PageResult<BillVo> pageResult = new PageResult<>(pageInfo.getRecords(), pageInfo.getTotal());
        return Result.ok(pageResult);
    }

}

