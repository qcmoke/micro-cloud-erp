package com.qcmoke.order.controller;

import com.qcmoke.common.entity.User;
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

    /**
     * 默认@AuthenticationPrincipal只能注入username，要实现注入用户信息，要配置AccessTokenConverter
     * 可以使用@AuthenticationPrincipal(expression = "#this.username")获取用户信息中的username
     */
    @GetMapping(value = "/get/{id}")
    public RespBean getOrder(@PathVariable("id") Integer id, @AuthenticationPrincipal User user) {
        log.info("user={}", user);
        return RespBean.ok(user.getUsername() + "查询了订单：order" + id);
    }
}