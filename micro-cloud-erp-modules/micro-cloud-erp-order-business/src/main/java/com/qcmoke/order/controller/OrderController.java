package com.qcmoke.order.controller;

import com.qcmoke.common.entity.CurrentUser;
import com.qcmoke.common.utils.RespBean;
import com.qcmoke.common.utils.ReturnResult;
import com.qcmoke.order.client.OrderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/business")
@RestController
public class OrderController {
    @Autowired
    private OrderClient orderClient;

    @RequestMapping(value = "/getOrder/{id}", method = RequestMethod.GET)
    ReturnResult<CurrentUser> get(@PathVariable("id") Integer id) {
        RespBean respBean = orderClient.getOrder(id);
        return new ReturnResult<CurrentUser>()
                .message("kk")
                .status(200)
                .data(new CurrentUser());
    }
}