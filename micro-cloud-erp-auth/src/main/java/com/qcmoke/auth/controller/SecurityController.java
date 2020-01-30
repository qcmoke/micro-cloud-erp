package com.qcmoke.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Slf4j
@RestController
public class SecurityController {

    @GetMapping("/user")
    public Principal currentUser(Principal principal) {
        log.info("获取用户信息Principal={}", principal);
        return principal;
    }
}