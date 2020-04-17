package com.qcmoke.wms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.BeanCopyUtil;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.wms.dto.StockQuery;
import com.qcmoke.wms.entity.Stock;
import com.qcmoke.wms.service.StockService;
import com.qcmoke.wms.vo.StockUpdateVo;
import com.qcmoke.wms.vo.StockVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-17
 */
@RestController
@RequestMapping("/stock")

public class StockController {

    @Autowired
    private StockService stockService;

    @GetMapping("/page")
    public Result<PageResult<StockVo>> page(PageQuery pageQuery, StockQuery query) {
        stockService.checkInitStock();
        Page<Stock> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        IPage<StockVo> pageInfo = stockService.getPage(page, query);
        PageResult<StockVo> pageResult = new PageResult<>(pageInfo.getRecords(), pageInfo.getTotal());
        return Result.ok(pageResult);
    }


    @PutMapping
    public Result<?> update(StockUpdateVo stockUpdateVo) {
        if (stockUpdateVo == null
                || StringUtils.isBlank(stockUpdateVo.getArea())
                || stockUpdateVo.getStockId() == null
                || stockUpdateVo.getSMax() == null
                || stockUpdateVo.getItemCount() == null) {
            throw new GlobalCommonException("参数不正确");
        }
        Stock stock = BeanCopyUtil.copy(stockUpdateVo, Stock.class);
        stock.setModifyTime(new Date());
        boolean flag = stockService.updateById(stock);
        if (!flag) {
            throw new GlobalCommonException("修改失败");
        }
        return Result.ok();
    }
}

