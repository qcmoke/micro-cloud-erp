package com.qcmoke.order.controller;

import com.qcmoke.common.service.OrderService;
import com.qcmoke.core.utils.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/business")
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/getOrder/{id}", method = RequestMethod.GET)
    RespBean get(@PathVariable("id") Integer id) {
        RespBean respBean = orderService.getOrder(id);
        return RespBean.ok(respBean);
    }
}