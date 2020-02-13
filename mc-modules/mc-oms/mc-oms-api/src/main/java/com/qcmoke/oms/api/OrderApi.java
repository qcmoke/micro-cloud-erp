package com.qcmoke.oms.api;

import com.qcmoke.common.dto.Result;
import com.qcmoke.oms.dto.OrderDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author qcmoke
 */
@RequestMapping("/order")
public interface OrderApi {
    @GetMapping(value = "/get/{username}")
    Result<List<OrderDto>> getOrder(@PathVariable("username") String username);
}