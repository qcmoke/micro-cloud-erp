package com.qcmoke.gateway.controller;

import com.alibaba.fastjson.JSONObject;
import com.qcmoke.common.utils.OAuthSecurityJwtUtil;
import com.qcmoke.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Slf4j
@RequestMapping("/gateway")
@RestController
public class GatewayController {

    @GetMapping("/userInfo")
    public Result currentUser(Principal principal, OAuth2Authentication oAuth2Authentication, HttpServletRequest request) {
        log.info("Principal={}", principal);
        log.info("OAuth2Authentication={}", oAuth2Authentication);
        JSONObject jsonObject = OAuthSecurityJwtUtil.getPrincipal(request);
        return Result.ok(jsonObject);
    }
}