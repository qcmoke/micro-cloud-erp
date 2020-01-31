package com.qcmoke.order.controller;

import com.qcmoke.common.utils.RespBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;


@Slf4j
@RequestMapping("/order")
@RestController
public class OrderController {

    /**
     * 从当前api的请求头里拿到token并设置到oAuth2RestTemplate执行时的请求头里，使得token可以在微服务间传递
     */
    @Autowired
    private OAuth2RestTemplate oAuth2RestTemplate;

    @GetMapping(value = "/get/{id}")
    public RespBean getOrder(@PathVariable("id") Integer id, @AuthenticationPrincipal String username) {
        log.info("username={}", username);
        return RespBean.ok(username + "查询了订单：order" + id);
    }

    @PostMapping("/placeAnOrder")
    public RespBean placeAnOrder(@AuthenticationPrincipal String username) {
        log.info("username={}", username);
        try {
            //扣减库存
            RespBean respBean = oAuth2RestTemplate.getForObject("http://127.0.0.1:8082/stock/reduce/1", RespBean.class);
            if (respBean == null || respBean.getStatus() != HttpStatus.OK.value()) {
                return RespBean.error("扣减库存失败");
            }
        } catch (RestClientException e) {
            return RespBean.error("扣减库存失败");
        }
        //生成订单
        return RespBean.ok(username + "下单成功");
    }
}