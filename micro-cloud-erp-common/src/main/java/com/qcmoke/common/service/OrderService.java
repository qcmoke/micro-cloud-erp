package com.qcmoke.common.service;

import com.qcmoke.common.utils.RespBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("MICRO-CLOUD-ERP-ORDER")
public interface OrderService {
    @RequestMapping(value = "/order/get/{id}")
    RespBean getOrder(@PathVariable("id") Integer id);
}
