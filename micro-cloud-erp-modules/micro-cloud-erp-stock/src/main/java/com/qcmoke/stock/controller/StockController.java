package com.qcmoke.stock.controller;

import com.qcmoke.common.utils.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/stock")
@RestController
public class StockController {

    @GetMapping(value = "/reduce/{id}")
    public RespBean getOrder(@PathVariable("id") Integer id, @AuthenticationPrincipal String username) {
        log.info("username={}", username);
        //减少库存量
        return RespBean.ok("减少成功");
    }

    @GetMapping("/testScopeWrite")
    @PreAuthorize("#oauth2.hasScope('write')")
    public RespBean testScopeWrite() {
        return RespBean.ok("write授权成功");
    }

    @GetMapping("/testScopeFly")
    @PreAuthorize("#oauth2.hasScope('fly')")
    public RespBean testScopeFly() {
        return RespBean.ok("fly授权成功");
    }

    @GetMapping("/testRoleAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public RespBean testRoleAdmin() {
        return RespBean.ok("ROLE_ADMIN授权成功");
    }

    @GetMapping("/testRoleUser")
    @PreAuthorize("hasRole('ROLE_USER')")
    public RespBean testRole() {
        return RespBean.ok("ROLE_USER授权成功");
    }


    @GetMapping("/demo")
    public Object demo() {
        return "demo";
    }

}
