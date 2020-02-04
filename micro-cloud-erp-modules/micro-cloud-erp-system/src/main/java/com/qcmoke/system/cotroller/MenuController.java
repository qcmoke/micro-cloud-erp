package com.qcmoke.system.cotroller;

import com.qcmoke.common.entity.router.VueRouter;
import com.qcmoke.common.utils.Result;
import com.qcmoke.common.utils.OAuthSecurityRedisUtil;
import com.qcmoke.system.entity.Menu;
import com.qcmoke.system.service.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Validated
@RestController
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private IMenuService menuService;

    @GetMapping("/getCurrentUserRouters")
    public Result getUserRouters() {
        String currentUsername = OAuthSecurityRedisUtil.getCurrentUsername();
        Map<String, Object> result = new HashMap<>();
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
}