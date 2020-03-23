package com.qcmoke.wms.client;

import com.qcmoke.common.constant.GlobalServiceListConstant;
import com.qcmoke.common.vo.Result;
import com.qcmoke.pms.api.PurchaseOrderMasterApi;
import com.qcmoke.pms.dto.PurchaseOrderMasterApiDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qcmoke
 */
@FeignClient(value = GlobalServiceListConstant.MC_PMS_SERVER_ID, fallbackFactory = PurchaseOrderMasterClient.ClientFallbackFactory.class)
public interface PurchaseOrderMasterClient extends PurchaseOrderMasterApi {

    @Slf4j
    @Component
    class ClientFallbackFactory implements FallbackFactory<PurchaseOrderMasterClient> {

        @Override
        public PurchaseOrderMasterClient create(Throwable throwable) {
            log.error("触发降级处理,e={}", throwable.getMessage());
            return new PurchaseOrderMasterClient() {

                @Override
                public Result<?> successForInItemToStock(List<Long> masterIdList) {
                    return Result.error(throwable.getMessage());
                }

                @Override
                public Result<?> checkCallBackForCreateStockPreReview(Long orderId, boolean b) {
                    return Result.error(throwable.getMessage());
                }

                @Override
                public Result<?> successForOutItemFromStock(PurchaseOrderMasterApiDto purchaseOrderMasterApiDto) {
                    return Result.error(throwable.getMessage());
                }
            };
        }
    }
}
