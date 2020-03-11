package com.qcmoke.ums.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.vo.Result;
import com.qcmoke.ums.dto.RoleDto;
import com.qcmoke.ums.entity.Role;
import com.qcmoke.ums.export.RoleExport;
import com.qcmoke.ums.service.RoleService;
import com.qcmoke.ums.vo.PageResult;
import com.wuwenze.poi.ExcelKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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

    @GetMapping
    public Result<PageResult> page(PageQuery pageQuery, Role role) {
        PageResult pageResult = roleService.getPage(pageQuery, role);
        return Result.ok(pageResult);
    }

    @GetMapping("/options")
    public Result<Object> roles() {
        List<Role> allRoles = roleService.findAllRoles();
        return Result.ok(allRoles);
    }

    @PutMapping
    public void updateRole(@Valid RoleDto roleDto) {
        this.roleService.updateRole(roleDto);
    }


    @GetMapping("/check/{roleName}")
    public boolean checkRoleName(@NotBlank(message = "{required}") @PathVariable String roleName) {
        Role result = this.roleService.getOne(new LambdaQueryWrapper<Role>().eq(Role::getRoleName, roleName));
        return result == null;
    }

    @PostMapping
    public void addRole(@Valid RoleDto roleDto) {
        this.roleService.createRole(roleDto);
    }

    @DeleteMapping("/{roleIds}")
    public void deleteRoles(@NotBlank(message = "{required}") @PathVariable String roleIds) {
        String[] ids = roleIds.split(StringPool.COMMA);
        this.roleService.deleteRoles(ids);
    }


    @PostMapping("/excel")
    public void export(PageQuery pageQuery, RoleDto roleDto, HttpServletResponse response) {
        List<RoleExport> records = this.roleService.findRoles(roleDto, pageQuery).getRecords();
        ExcelKit.$Export(RoleExport.class, response).downXlsx(records, false);
    }

}

