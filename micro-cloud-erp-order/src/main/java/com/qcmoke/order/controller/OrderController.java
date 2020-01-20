package com.qcmoke.order.controller;

import com.qcmoke.core.utils.RespBean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrderController {
    @RequestMapping(value = "/order/get/{id}")
    RespBean get(@PathVariable("id") Integer id) {
        return RespBean.ok("order" + id);
    }
}