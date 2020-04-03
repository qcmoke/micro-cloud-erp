package com.qcmoke.wms.client;

import com.qcmoke.common.constant.GlobalServiceListConstant;
import com.qcmoke.common.vo.Result;
import com.qcmoke.oms.api.SaleOrderMasterApi;
import com.qcmoke.oms.dto.SaleOrderMasterApiDto;
import com.qcmoke.wms.constant.StockType;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qcmoke
 */
@FeignClient(value = GlobalServiceListConstant.MC_OMS_SERVER_ID, fallbackFactory = SaleOrderMasterClient.ClientFallbackFactory.class)
public interface SaleOrderMasterClient extends SaleOrderMasterApi {
    @Slf4j
    @Component
    class ClientFallbackFactory implements FallbackFactory<SaleOrderMasterClient> {

        @Override
        public SaleOrderMasterClient create(Throwable throwable) {
            log.error("触发降级处理,e={}", throwable.getMessage());
            return new SaleOrderMasterClient() {
                @Override
                public Result<?> successForInItemToStock(List<Long> orderList) {
                    return Result.error(throwable.getMessage());
                }

                @Override
                public Result<?> successForOutItemFromStock(SaleOrderMasterApiDto saleOrderMasterApiDto) {
                    return Result.error(throwable.getMessage());
                }

                @Override
                public Result<?> checkCallBackForCreateStockItem(StockType stockType, Long orderId, boolean isOk) {
                    return Result.error(throwable.getMessage());
                }
            };
        }
    }
}
