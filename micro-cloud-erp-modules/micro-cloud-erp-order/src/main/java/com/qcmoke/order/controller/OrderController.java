package com.qcmoke.order.controller;

import com.qcmoke.core.utils.RespBean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/order")
@RestController
public class OrderController {
    @GetMapping(value = "/get/{id}")
    public RespBean getOrder(@PathVariable("id") Integer id, @AuthenticationPrincipal String username) {
        return RespBean.ok(username + "查询了订单：order" + id);
    }
}