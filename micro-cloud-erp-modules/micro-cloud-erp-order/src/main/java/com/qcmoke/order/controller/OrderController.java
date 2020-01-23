package com.qcmoke.order.controller;

import com.qcmoke.core.entity.TokenInfo;
import com.qcmoke.core.utils.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequestMapping("/order")
@RestController
public class OrderController {


    @GetMapping(value = "/get/{id}")
    public RespBean getOrder(@PathVariable("id") Integer id, @RequestHeader String username, TokenInfo tokenInfo) {
        log.info("username={}", username);
        log.info("tokenInfo={}", tokenInfo);
        return RespBean.ok(username + "查询了订单：order" + id);
    }
}