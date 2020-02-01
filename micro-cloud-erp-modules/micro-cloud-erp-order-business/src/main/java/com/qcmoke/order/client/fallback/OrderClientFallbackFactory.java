package com.qcmoke.order.client.fallback;

import com.qcmoke.order.client.OrderClient;
import com.qcmoke.common.utils.RespBean;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderClientFallbackFactory implements FallbackFactory<OrderClient> {
    @Override
    public OrderClient create(Throwable throwable) {
        return new OrderClient() {
            @Override
            public RespBean getOrder(Integer id) {
                log.info("id={} e={}", id, throwable.getMessage());
                return RespBean.error(throwable.getMessage());
            }
        };
    }
}
