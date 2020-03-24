package com.qcmoke.oms.client;

import com.qcmoke.common.constant.GlobalServiceListConstant;
import com.qcmoke.common.vo.Result;
import com.qcmoke.fms.api.BillApi;
import com.qcmoke.fms.dto.BillApiDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author qcmoke
 */
@FeignClient(value = GlobalServiceListConstant.MC_FMS_SERVER_ID, fallbackFactory = BillClient.ClientFallbackFactory.class)
public interface BillClient extends BillApi {

    @Slf4j
    @Component
    class ClientFallbackFactory implements FallbackFactory<BillClient> {
        @Override
        public BillClient create(Throwable throwable) {
            log.error("触发降级处理,e={}", throwable.getMessage());
            return new BillClient() {
                @Override
                public Result<?> addBill(BillApiDto billApiDto) {
                    return Result.error(throwable.getMessage());
                }
            };
        }
    }
}
