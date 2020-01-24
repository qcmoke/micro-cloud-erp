package com.qcmoke.order.controller;

import com.qcmoke.core.utils.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequestMapping("/order")
@RestController
public class OrderController {


    @GetMapping(value = "/get/{id}")
    public RespBean getOrder(@PathVariable("id") Integer id, @AuthenticationPrincipal String username) {
        log.info("username={}", username);
        return RespBean.ok(username + "查询了订单：order" + id);
    }
}