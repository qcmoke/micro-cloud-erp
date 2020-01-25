package com.qcmoke.stock.controller;

import com.qcmoke.core.utils.RespBean;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/demo")
    public Object demo() {
        return "demo";
    }

}
