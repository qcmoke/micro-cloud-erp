package com.qcmoke.ums.controller;


import com.qcmoke.common.vo.Result;
import com.qcmoke.ums.entity.Role;
import com.qcmoke.ums.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author qcmoke
 * @since 2020-02-12
 */
@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;


    @GetMapping("options")
    public Result<Object> roles() {
        List<Role> allRoles = roleService.findAllRoles();
        return Result.ok(allRoles);
    }
}

