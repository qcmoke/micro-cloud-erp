package com.qcmoke.pms.client;

import com.qcmoke.common.constant.GlobalServiceListConstant;
import com.qcmoke.common.vo.Result;
import com.qcmoke.wms.api.StockItemApi;
import com.qcmoke.wms.dto.StockItemDto;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author qcmoke
 */


@FeignClient(value = GlobalServiceListConstant.MC_WMS_SERVER_ID, fallbackFactory = MaterialStockClient.MaterialStockClientFallbackFactory.class)
public interface MaterialStockClient extends StockItemApi {

    @Slf4j
    @Component
    class MaterialStockClientFallbackFactory implements FallbackFactory<MaterialStockClient> {
        @Override
        public MaterialStockClient create(Throwable throwable) {
            log.error("触发降级处理,e={}", throwable.getMessage());
            return new MaterialStockClient() {
                @Override
                public Result<?> createStockPreReview(StockItemDto stockItemDto) {
                    return Result.error();
                }
            };
        }
    }
}

