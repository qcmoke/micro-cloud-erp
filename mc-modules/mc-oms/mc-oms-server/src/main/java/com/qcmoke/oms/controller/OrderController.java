package com.qcmoke.oms.controller;

import com.qcmoke.common.vo.Result;
import com.qcmoke.oms.dto.OrderDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * @author qcmoke
 */
@Slf4j
@RequestMapping("/order")
@RestController
public class OrderController {

    @GetMapping(value = "/get/{username}")
    public Result<List<OrderDto>> getOrder(@PathVariable("username") String username) {
        log.info("username={}", username);
        OrderDto orderDto = new OrderDto();
        orderDto.setId(1);
        orderDto.setName("笔记本");
        return Result.ok(Collections.singletonList(orderDto));
    }
}
