package com.qcmoke.order.controller;

import com.qcmoke.common.dto.OrderDto;
import com.qcmoke.common.utils.OAuthSecurityJwtUtil;
import com.qcmoke.common.utils.Result;
import com.qcmoke.common.utils.RpcResult;
import com.qcmoke.order.client.OrderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/business")
@RestController
public class OrderController {
    @Autowired
    private OrderClient orderClient;

    @GetMapping("/getOrderByUserName")
    public Result get(HttpServletRequest request) {
        String username = OAuthSecurityJwtUtil.getCurrentUsername(request);
        RpcResult<List<OrderDto>> result = orderClient.getOrder(username);
        if (result.getStatus() == RpcResult.ERROR_STATUS) {
            return Result.error(result.getMessage());
        }
        return Result.ok(result.getData());
    }
}