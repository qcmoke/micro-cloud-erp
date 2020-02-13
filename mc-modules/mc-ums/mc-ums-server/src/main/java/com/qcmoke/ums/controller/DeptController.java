package com.qcmoke.ums.controller;


import com.qcmoke.common.dto.Result;
import com.qcmoke.common.vo.PageQuery;
import com.qcmoke.ums.dto.PageResult;
import com.qcmoke.ums.entity.Dept;
import com.qcmoke.ums.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Result<Object> deptList(PageQuery pageQuery, Dept dept) {
        PageResult pageResult = deptService.queryDeptList(pageQuery, dept);
        return Result.ok(pageResult);
    }
}

