package com.qcmoke.order.client.fallback;

import com.qcmoke.common.dto.OrderDto;
import com.qcmoke.common.utils.RpcResult;
import com.qcmoke.order.client.OrderClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务降级处理
 */
@Slf4j
@Component
public class OrderClientFallbackFactory implements FallbackFactory<OrderClient> {
    @Override
    public OrderClient create(Throwable e) {
        return new OrderClient() {
            @Override
            public RpcResult<List<OrderDto>> getOrder(String username) {
                log.error("远程调用失败", e);
                return new RpcResult<List<OrderDto>>().error("远程调用失败, e=" + e.getMessage());
            }
        };
    }
}
