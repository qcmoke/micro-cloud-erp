package com.qcmoke.ums.controller;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.utils.WebUtil;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.Result;
import com.qcmoke.ums.constant.MenuConstant;
import com.qcmoke.ums.entity.Menu;
import com.qcmoke.ums.export.MenuExport;
import com.qcmoke.ums.service.MenuService;
import com.qcmoke.ums.vo.PageResult;
import com.qcmoke.ums.vo.VueRouter;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;


/**
 * @author qcmoke
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    @GetMapping("/getCurrentUserRouters")
    public Result<Object> getUserRouters(HttpServletRequest request) {
        String currentUsername = OauthSecurityJwtUtil.getCurrentUsername(request);
        Map<String, Object> result = new HashMap<>(2);
        List<VueRouter<Menu>> userRouters = this.menuService.getUserRouters(currentUsername);
        String userPermissions = this.menuService.findUserPermissions(currentUsername);
        String[] permissionArray = new String[0];
        if (StringUtils.isNoneBlank(userPermissions)) {
            permissionArray = StringUtils.splitByWholeSeparatorPreserveAllTokens(userPermissions, ",");
        }
        result.put("routes", userRouters);
        result.put("permissions", permissionArray);
        return Result.ok(result);
    }


    @GetMapping
    public Result<Object> page(Menu menu) {
        PageResult pageResult = menuService.getPage(menu);
        return Result.ok(pageResult);
    }


    @GetMapping("/permissions")
    public String findUserPermissions(String username) {
        return this.menuService.findUserPermissions(username);
    }

    @PostMapping
    public void addMenu(@Valid Menu menu) {
        menu.setCreateTime(new Date());
        intMenuType(menu);
        this.menuService.save(menu);
    }

    private void intMenuType(Menu menu) {
        if (menu.getParentId() == null) {
            menu.setParentId(0L);
        }
        if (MenuConstant.TYPE_BUTTON.equals(menu.getType())) {
            menu.setPath(null);
            menu.setIcon(null);
            menu.setComponent(null);
            menu.setOrderNum(null);
        }
    }


    @DeleteMapping("/{menuIds}")
    public void deleteMenus(@NotBlank(message = "{required}") @PathVariable String menuIds) {
        List<Long> idList = WebUtil.parseIdStrToLongList(menuIds);
        if (CollectionUtils.isEmpty(idList)){
            throw  new GlobalCommonException("没有可删除的menu");
        }
        this.menuService.removeMenusByIdList(idList);
    }

    @PutMapping
    public void updateMenu(@Valid Menu menu) {
        menu.setModifyTime(new Date());
        intMenuType(menu);
        this.menuService.updateById(menu);
    }

    @PostMapping("/excel")
    public void export(Menu menu, HttpServletResponse response) {
        List<MenuExport> menus = this.menuService.findMenuList(menu);
        ExcelKit.$Export(Menu.class, response).downXlsx(menus, false);
    }
}