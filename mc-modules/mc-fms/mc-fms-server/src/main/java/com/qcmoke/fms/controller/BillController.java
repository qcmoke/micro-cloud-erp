package com.qcmoke.fms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.fms.api.BillApi;
import com.qcmoke.fms.dto.BillApiDto;
import com.qcmoke.fms.dto.BillQuery;
import com.qcmoke.fms.entity.Bill;
import com.qcmoke.fms.service.BillService;
import com.qcmoke.fms.vo.BillVo;
import com.qcmoke.fms.vo.StatisticsDataVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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


    @Override
    @PostMapping(value = "/addBill", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Result<?> addBill(@RequestBody BillApiDto billApiDto) {
        billService.addBill(billApiDto);
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
    public Result<PageResult<BillVo>> page(PageQuery page, BillQuery query) {
        IPage<BillVo> pageInfo = billService.getPage(new Page<Bill>(page.getPageNum(), page.getPageSize()), query);
        PageResult<BillVo> pageResult = new PageResult<>(pageInfo.getRecords(), pageInfo.getTotal());
        return Result.ok(pageResult);
    }

}

