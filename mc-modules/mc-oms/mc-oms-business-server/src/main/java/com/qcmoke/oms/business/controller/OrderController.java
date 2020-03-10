package com.qcmoke.oms.business.controller;

import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.Result;
import com.qcmoke.oms.business.client.OrderClient;
import com.qcmoke.oms.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author qcmoke
 */
@RequestMapping("/business")
@RestController
public class OrderController {
    @Autowired
    private OrderClient orderClient;

    @GetMapping("/getOrderByUserName")
    public Result<Object> get(HttpServletRequest request) {
        String username = OauthSecurityJwtUtil.getCurrentUsername(request);
        Result<List<OrderDto>> result = orderClient.getOrder(username);
        if (result.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return Result.error(result.getMessage());
        }
        return Result.ok(result.getData());
    }
}