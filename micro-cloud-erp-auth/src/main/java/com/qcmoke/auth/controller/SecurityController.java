package com.qcmoke.auth.controller;

import com.qcmoke.auth.exception.ValidateCodeException;
import com.qcmoke.auth.properties.Oauth2SecurityProperties;
import com.qcmoke.auth.service.ValidateCodeService;
import com.qcmoke.common.utils.OAuthSecurityJwtUtil;
import com.qcmoke.common.utils.OAuthSecurityRedisUtil;
import com.qcmoke.common.utils.OAuthSecurityUtil;
import com.qcmoke.common.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequestMapping("/resource")
@RestController
public class SecurityController {

    @Autowired
    private ValidateCodeService validateCodeService;

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @Autowired
    private Oauth2SecurityProperties oauth2SecurityProperties;

    /**
     * 获取认证完成的用户信息
     */
    @GetMapping("/user")
    public Object currentUser(HttpServletRequest request) {
        if (oauth2SecurityProperties.getEnableJwt()) {
            return OAuthSecurityJwtUtil.getPrincipal(request);
        }
        return OAuthSecurityRedisUtil.getOAuth2Authentication().getPrincipal();
    }

    /**
     * 获取验证码
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
        validateCodeService.create(request, response);
    }


    /**
     * 登出
     */
    @DeleteMapping(value = "/logout")
    public Result revokeToken(HttpServletRequest request) {
        if (consumerTokenServices.revokeToken(OAuthSecurityUtil.getAccessToken(request))) {
            return Result.ok("注销成功");
        } else {
            return Result.error("注销失败");
        }
    }
}