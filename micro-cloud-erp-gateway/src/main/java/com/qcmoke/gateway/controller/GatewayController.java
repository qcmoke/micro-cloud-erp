package com.qcmoke.gateway.controller;

import com.qcmoke.common.utils.RespBean;
import com.qcmoke.common.utils.SecurityOAuth2Util;
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
            put("currentTokenValue", SecurityOAuth2Util.getCurrentTokenValue());
            put("currentUserAuthority", SecurityOAuth2Util.getCurrentUserAuthority());
            put("currentUsername", SecurityOAuth2Util.getCurrentUsername());
            put("authentication", SecurityContextHolder.getContext().getAuthentication());
            put("CurrentUser", SecurityOAuth2Util.getCurrentUser());
            put("principal", principal);
        }};
        return RespBean.ok(userInfo);
    }
}