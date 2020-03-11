package com.qcmoke.ums.controller;


import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.vo.Result;
import com.qcmoke.ums.entity.Dept;
import com.qcmoke.ums.export.DeptExport;
import com.qcmoke.ums.service.DeptService;
import com.qcmoke.ums.vo.PageResult;
import com.wuwenze.poi.ExcelKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author qcmoke
 * @since 2020-02-12
 */
@RestController
@RequestMapping("/dept")
public class DeptController {
    @Autowired
    private DeptService deptService;

    @GetMapping
    public Result<PageResult> page(PageQuery pageQuery, Dept dept) {
        PageResult pageResult = deptService.queryDeptList(pageQuery, dept);
        return Result.ok(pageResult);
    }


    @PostMapping
    public void addDept(@Valid Dept dept) {
        if (dept.getParentId() == null) {
            dept.setParentId(0L);
        }
        dept.setCreateTime(new Date());
        deptService.save(dept);
    }

    @DeleteMapping("/{deptIds}")
    public void deleteDepts(@PathVariable String deptIds) {
        deptService.removeByIds(Arrays.asList(deptIds.split(StringPool.COMMA)));
    }

    @PutMapping
    public void updateDept(@Valid Dept dept) {
        if (dept.getParentId() == null) {
            dept.setParentId(0L);
        }
        dept.setModifyTime(new Date());
        this.deptService.updateById(dept);
    }

    @PostMapping("/excel")
    public void export(Dept dept, PageQuery pageQuery, HttpServletResponse response) {
        List<DeptExport> deptExportList = this.deptService.findDepts(dept, pageQuery);
        ExcelKit.$Export(DeptExport.class, response).downXlsx(deptExportList, false);
    }


}

