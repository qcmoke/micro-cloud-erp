package com.qcmoke.order.provider;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.qcmoke.common.dto.OrderDto;
import com.qcmoke.common.utils.RpcResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;


@Slf4j
@RequestMapping("/rpc/order")
@RestController
public class OrderProvider {

    @GetMapping(value = "/get/{username}")
    @HystrixCommand(fallbackMethod = "getOrderFallback")
    public RpcResult<List<OrderDto>> getOrder(@PathVariable("username") String username) {
        log.info("username={}", username);
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1);
        orderDto.setName("笔记本");

        if ("qcmoke".equals(username)) {
            throw new RuntimeException("发送异常");
        }
        return new RpcResult<List<OrderDto>>().success(Collections.singletonList(orderDto));
    }

    /**
     * 服务熔断处理
     */
    public RpcResult<List<OrderDto>> getOrderFallback(String username) {
        String msg = "查询失败，触发服务熔断处理!";
        log.error(msg);
        return new RpcResult<List<OrderDto>>().error(msg);
    }

}