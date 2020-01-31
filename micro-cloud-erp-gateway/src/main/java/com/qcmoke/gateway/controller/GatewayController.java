package com.qcmoke.gateway.controller;

import com.qcmoke.common.utils.RespBean;
import com.qcmoke.gateway.utils.GatewayUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/gateway")
@RestController
public class GatewayController {

    @GetMapping("/userInfo")
    public RespBean currentUser(Principal principal, OAuth2Authentication auth) {

        Map map = (Map) auth.getUserAuthentication().getDetails();
        System.out.println(map);

        HashMap<String, Object> userInfo = new HashMap<String, Object>() {{
            put("currentTokenValue", GatewayUtil.getCurrentTokenValue());
            put("currentUserAuthority", GatewayUtil.getCurrentUserAuthority());
            put("currentUsername", GatewayUtil.getCurrentUsername());
            put("authentication", SecurityContextHolder.getContext().getAuthentication());
            put("CurrentUser", GatewayUtil.getCurrentUser());
            put("principal", principal);
        }};
        return RespBean.ok(userInfo);
    }
}