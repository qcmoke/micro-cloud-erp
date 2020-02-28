package com.qcmoke.auth.controller;

import com.qcmoke.auth.common.utils.OauthSecurityRedisUtil;
import com.qcmoke.auth.vo.UserDetailVo;
import com.qcmoke.auth.exception.ValidateCodeException;
import com.qcmoke.auth.properties.Oauth2SecurityProperties;
import com.qcmoke.auth.service.UserService;
import com.qcmoke.auth.service.ValidateCodeService;
import com.qcmoke.common.vo.Result;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.utils.oauth.OauthSecurityUtil;
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

/**
 * @author qcmoke
 */
@Slf4j
@RequestMapping("/resource")
@RestController
public class ResourceSecurityController {
    @Autowired
    private ValidateCodeService validateCodeService;
    @Autowired
    private ConsumerTokenServices consumerTokenServices;
    @Autowired
    private Oauth2SecurityProperties oauth2SecurityProperties;
    @Autowired
    private UserService userService;

    /**
     * 获取认证完成的用户信息
     */
    @GetMapping("/user")
    public Object currentUser(HttpServletRequest request) {
        if (oauth2SecurityProperties.getEnableJwt()) {
            return OauthSecurityJwtUtil.getCurrentUser(request);
        }
        return OauthSecurityRedisUtil.getOAuth2Authentication().getPrincipal();
    }


    /**
     * 获取认证完成的用户信息
     */
    @GetMapping("/user/detail")
    public Result<Object> getUserDetail(HttpServletRequest request) {
        String currentUsername = OauthSecurityJwtUtil.getCurrentUsername(request);
        UserDetailVo userDetailVo = userService.getUserDetailByUsername(currentUsername);
        return Result.ok(userDetailVo);
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
    public Result<Object> revokeToken(HttpServletRequest request) {
        if (consumerTokenServices.revokeToken(OauthSecurityUtil.getBearerToken(request))) {
            return Result.ok("注销成功");
        } else {
            return Result.error("注销失败");
        }
    }
}