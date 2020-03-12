package com.qcmoke.pms.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.vo.PageResult;
import com.qcmoke.common.vo.Result;
import com.qcmoke.pms.entity.Material;
import com.qcmoke.pms.service.MaterialService;
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
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materielService;

    @GetMapping
    public Result<PageResult<Material>> page(PageQuery pageQuery) {
        IPage<Material> page = materielService.page(new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize()));
        PageResult<Material> pageResult = new PageResult<>(page.getRecords(), page.getTotal());
        return Result.ok(pageResult);
    }

    @GetMapping("/{id}")
    public Result<Material> one(@PathVariable String id) {
        Material materielVo = materielService.getById(id);
        return Result.ok(materielVo);
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public void add(Material material) {
        materielService.save(material);
    }

    @DeleteMapping
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Integer> ids) {
        materielService.removeByIds(ids);
    }

    @PutMapping
    @Transactional(rollbackFor = Exception.class)
    public void update(Material material) {
        materielService.updateById(material);
    }



}

