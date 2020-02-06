package com.qcmoke.oms.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.qcmoke.common.vo.Result;
import com.qcmoke.oms.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@RequestMapping("/order")
@RestController
public class OrderController {

    @GetMapping(value = "/get/{username}")
    @HystrixCommand(fallbackMethod = "getOrderFallback")
    public Result<List<OrderDto>> getOrder(@PathVariable("username") String username) {
        log.info("username={}", username);
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1);
        orderDto.setName("笔记本");

        if ("qcmoke".equals(username)) {
            throw new RuntimeException("发送异常");
        }
        return Result.ok(Collections.singletonList(orderDto));
    }

    /**
     * 服务熔断处理
     */
    public Result<List<OrderDto>> getOrderFallback(String username) {
        String msg = "查询失败，触发服务熔断处理!";
        log.error(msg);
        return Result.error(msg);
    }
}
