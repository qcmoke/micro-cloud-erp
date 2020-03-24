package com.qcmoke.pms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.pms.entity.MaterialRefund;
import com.qcmoke.pms.service.MaterialRefundService;
import com.qcmoke.pms.vo.MaterialRefundVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/materialRefund")
public class MaterialRefundController {

    @Autowired
    private MaterialRefundService materialRefundService;

    @GetMapping
    public Result<PageResult<MaterialRefundVo>> page(PageQuery pageQuery, MaterialRefund materialDto) {
        Page<MaterialRefund> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        PageResult<MaterialRefundVo> pageResult = materialRefundService.getPage(page, materialDto);
        return Result.ok(pageResult);
    }

    @PostMapping
    public Result<Boolean> createRefuse(MaterialRefund materialRefund) {
        if (materialRefund == null || materialRefund.getPurchaseOrderMasterId() == null) {
            throw new GlobalCommonException("some params are required");
        }
        materialRefundService.createRefuse(materialRefund);
        return Result.ok();
    }


    @DeleteMapping("/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delete(@PathVariable String ids) {
        List<Long> idList = WebUtil.parseIdStrToLongList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            throw new GlobalCommonException("ids is required");
        }
        boolean status = materialRefundService.removeByIds(idList);
        return status ? Result.ok() : Result.error();
    }

}

