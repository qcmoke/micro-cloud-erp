package com.qcmoke.wms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.wms.entity.StockItem;
import com.qcmoke.wms.service.StockItemService;
import com.qcmoke.wms.vo.StockItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author qcmoke
 * @since 2020-03-17
 */
@RestController
@RequestMapping("/stockItem")
public class StockItemController {

    @Autowired
    private StockItemService stockItemService;


    @GetMapping("/page")
    public Result<PageResult<StockItemVo>> page(PageQuery pageQuery, StockItem stockItemDto) {
        Page<StockItem> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        IPage<StockItemVo> pageInfo = stockItemService.getPage(page, stockItemDto);
        PageResult<StockItemVo> pageResult = new PageResult<>(pageInfo.getRecords(), pageInfo.getTotal());
        return Result.ok(pageResult);
    }


    @PutMapping("/checkPass")
    public Result<Boolean> checkPass(Long stockItemId) {
        return stockItemService.updateStatus(stockItemId, 3);
    }

    @PutMapping("/checkFail")
    public Result<Boolean> checkFail(Long stockItemId) {
        return stockItemService.updateStatus(stockItemId, 2);
    }

}

