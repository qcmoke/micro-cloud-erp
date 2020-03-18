package com.qcmoke.pms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.pms.entity.MaterialRefund;
import com.qcmoke.pms.service.MaterialRefundService;
import com.qcmoke.pms.vo.MaterialRefundVo;
import org.apache.commons.collections.CollectionUtils;
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
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> createRefuse(MaterialRefund materialRefund) {
        if (materialRefund == null || materialRefund.getPurchaseOrderMasterId() == null) {
            throw new GlobalCommonException("退订失败，缺少订单id。");
        }
        Long currentUserId = OauthSecurityJwtUtil.getCurrentUserId();
        materialRefund.setCreateUserId(currentUserId);
        materialRefund.setCreateTime(new Date());
        materialRefund.setStatus(1);
        materialRefund.setCheckStatus(1);
        boolean save = materialRefundService.save(materialRefund);
        return Result.ok(save);
    }


    @PutMapping("/checkFail")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> checkFail(Long refundId) {
        return updateStatus(refundId, 2);
    }


    @PutMapping("/checkPass")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> checkPass(Long refundId) {
        return updateStatus(refundId, 3);
    }

    private Result<Boolean> updateStatus(Long refundId, int i) {
        if (refundId == null) {
            throw new GlobalCommonException("refundId is required");
        }
        Long currentUserId = OauthSecurityJwtUtil.getCurrentUserId();
        MaterialRefund materialRefund = new MaterialRefund();
        materialRefund.setRefundId(refundId);
        materialRefund.setModifyTime(new Date());
        materialRefund.setCheckUserId(currentUserId);
        materialRefund.setCheckStatus(i);
        boolean flag = materialRefundService.updateById(materialRefund);
        return Result.ok(flag);
    }


    @PutMapping("/toShip")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> toShip(Long refundId) {
        if (refundId == null) {
            throw new GlobalCommonException("refundId is required");
        }
        Long currentUserId = OauthSecurityJwtUtil.getCurrentUserId();
        MaterialRefund materialRefund = new MaterialRefund();
        materialRefund.setRefundId(refundId).setModifyTime(new Date()).setCheckUserId(currentUserId).setOutDate(new Date()).setStatus(1);
        boolean flag = materialRefundService.updateById(materialRefund);
        return Result.ok(flag);
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

