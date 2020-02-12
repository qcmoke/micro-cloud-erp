package com.qcmoke.ums.cotroller;

import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.Result;
import com.qcmoke.ums.entity.Menu;
import com.qcmoke.ums.service.MenuService;
import com.qcmoke.ums.vo.VueRouter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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