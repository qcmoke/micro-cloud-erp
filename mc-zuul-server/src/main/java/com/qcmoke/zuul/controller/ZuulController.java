package com.qcmoke.zuul.controller;

import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * @author qcmoke
 */
@Slf4j
@RequestMapping("/gateway")
@RestController
public class ZuulController {

    @GetMapping("/userInfo")
    public Result<Object> currentUser(Principal principal, OAuth2Authentication oAuth2Authentication, HttpServletRequest request) {
        log.info("Principal={}", principal);
        log.info("OAuth2Authentication={}", oAuth2Authentication);
        return Result.ok(OauthSecurityJwtUtil.getCurrentUser(request));
    }
}