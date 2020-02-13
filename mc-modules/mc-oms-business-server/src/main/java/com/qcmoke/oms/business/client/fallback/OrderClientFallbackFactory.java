package com.qcmoke.oms.business.client.fallback;

import com.qcmoke.common.dto.Result;
import com.qcmoke.oms.business.client.OrderClient;
import com.qcmoke.oms.dto.OrderDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务降级处理
 * @author qcmoke
 */
@Slf4j
@Component
public class OrderClientFallbackFactory implements FallbackFactory<OrderClient> {
    @Override
    public OrderClient create(Throwable e) {
        return new OrderClient() {
            @Override
            public Result<List<OrderDto>> getOrder(String username) {
                log.error("远程调用失败", e);
                return Result.error("远程调用失败, e=" + e.getMessage());
            }
        };
    }
}
