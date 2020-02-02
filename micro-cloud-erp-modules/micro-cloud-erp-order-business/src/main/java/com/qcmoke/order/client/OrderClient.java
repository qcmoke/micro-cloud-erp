package com.qcmoke.order.client;

import com.qcmoke.common.constant.GlobalServiceListConstant;
import com.qcmoke.common.dto.OrderDto;
import com.qcmoke.common.utils.RpcResult;
import com.qcmoke.order.client.fallback.OrderClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = GlobalServiceListConstant.MICRO_CLOUD_ERP_ORDER, fallbackFactory = OrderClientFallbackFactory.class)
public interface OrderClient {
    @GetMapping("/rpc/order/get/{username}")
    RpcResult<List<OrderDto>> getOrder(@PathVariable("username") String username);
}
