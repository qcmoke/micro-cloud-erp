package com.qcmoke.order.client;

import com.qcmoke.common.constant.GlobalServiceListConstant;
import com.qcmoke.common.utils.RespBean;
import com.qcmoke.order.client.fallback.OrderClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = GlobalServiceListConstant.MICRO_CLOUD_ERP_ORDER, fallbackFactory = OrderClientFallbackFactory.class)
public interface OrderClient {
    @RequestMapping(value = "/order/get/{id}")
    RespBean getOrder(@PathVariable("id") Integer id);
}
