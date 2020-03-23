package com.qcmoke.pms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.pms.entity.Material;
import com.qcmoke.pms.service.MaterialService;
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
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materielService;


    @GetMapping("/getAll")
    public Result<List<Material>> getAll() {
        List<Material> all = materielService.list();
        return Result.ok(all);
    }


    @GetMapping("/getAll/{supplierId}")
    public Result<List<Material>> getAllBySupplierId(@PathVariable Long supplierId) {
        if (supplierId == null) {
            throw new GlobalCommonException("supplierId is required");
        }
        List<Material> all = materielService.getAllBySupplierId(supplierId);
        return Result.ok(all);
    }

    @GetMapping
    public Result<PageResult<Material>> page(PageQuery pageQuery, Material materialDto) {
        IPage<Material> pageInfo = materielService.page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()), new QueryWrapper<>(materialDto));
        PageResult<Material> pageResult = new PageResult<>(pageInfo.getRecords(), pageInfo.getTotal());
        return Result.ok(pageResult);
    }

    @GetMapping("/{materialId}")
    public Result<Material> one(@PathVariable String materialId) {
        if (StringUtils.isBlank(materialId)) {
            throw new GlobalCommonException("id is required");
        }
        Material materielVo = materielService.getById(materialId);
        return Result.ok(materielVo);
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> add(Material material) {
        if (StringUtils.isBlank(material.getMaterialName()) || StringUtils.isBlank(material.getUnit())) {
            throw new GlobalCommonException("materialName or unit is required");
        }
        material.setCreateTime(new Date());
        boolean save = materielService.save(material);
        return Result.ok(save);
    }

    @DeleteMapping("/{ids}")
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> delete(@PathVariable String ids) {
        List<Long> idList = WebUtil.parseIdStrToLongList(ids);
        if (CollectionUtils.isEmpty(idList)) {
            throw new GlobalCommonException("materialIds is required");
        }
        boolean status = materielService.removeByIds(idList);
        return status ? Result.ok() : Result.error();
    }


    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public Result<Boolean> update(Material material) {
        if (StringUtils.isBlank(material.getMaterialName()) || StringUtils.isBlank(material.getUnit())) {
            throw new GlobalCommonException("materialName or unit is required");
        }
        material.setModifyTime(new Date());
        boolean flag = materielService.updateById(material);
        return flag ? Result.ok() : Result.error();
    }


}

