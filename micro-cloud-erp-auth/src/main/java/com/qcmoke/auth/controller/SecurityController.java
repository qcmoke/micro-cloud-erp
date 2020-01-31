package com.qcmoke.auth.controller;

import com.qcmoke.auth.exception.ValidateCodeException;
import com.qcmoke.auth.service.ValidateCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@Slf4j
@RestController
public class SecurityController {

    @Autowired
    private ValidateCodeService validateCodeService;


    /**
     * 获取认证完成的用户信息
     */
    @GetMapping("/user")
    public Principal currentUser(Principal principal) {
        log.info("获取用户信息Principal={}", principal);
        return principal;
    }

    /**
     * 获取验证码
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws IOException, ValidateCodeException {
        validateCodeService.create(request, response);
    }
}