package com.qcmoke.common.service.fallback;

import com.qcmoke.common.service.OrderService;
import com.qcmoke.common.utils.RespBean;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderServiceFallbackFactory implements FallbackFactory<OrderService> {
    @Override
    public OrderService create(Throwable throwable) {
        return new OrderService() {
            @Override
            public RespBean getOrder(Integer id) {
                log.info("id={} e={}", id, throwable.getMessage());
                return RespBean.error(throwable.getMessage());
            }
        };
    }
}
