package com.qcmoke.wms.controller;

import com.qcmoke.common.dto.Result;
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
    public Result<Object> getOrder(@PathVariable("id") Integer id, @AuthenticationPrincipal String username) {
        log.info("username={}", username);
        //减少库存量
        return Result.ok("减少成功");
    }

    @GetMapping("/testScopeWrite")
    @PreAuthorize("#oauth2.hasScope('write')")
    public Result<Object> testScopeWrite() {
        return Result.ok("write授权成功");
    }

    @GetMapping("/testScopeFly")
    @PreAuthorize("#oauth2.hasScope('fly')")
    public Result<Object> testScopeFly() {
        return Result.ok("fly授权成功");
    }

    @GetMapping("/testRoleAdmin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Result<Object> testRoleAdmin() {
        return Result.ok("ROLE_ADMIN授权成功");
    }

    @GetMapping("/testRoleUser")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Result<Object> testRole() {
        return Result.ok("ROLE_USER授权成功");
    }


    @GetMapping("/demo")
    @PreAuthorize("hasAuthority('user:view')")
    public Result<Object> demo() {
        return Result.ok("demo");
    }

}
