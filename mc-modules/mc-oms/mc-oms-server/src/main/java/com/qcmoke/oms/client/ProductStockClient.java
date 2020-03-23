package com.qcmoke.oms.client;

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
@FeignClient(value = GlobalServiceListConstant.MC_WMS_SERVER_ID, fallbackFactory = ProductStockClient.ProductStockClientFallbackFactory.class)
public interface ProductStockClient extends StockItemApi {
    @Slf4j
    @Component
    class ProductStockClientFallbackFactory implements FallbackFactory<ProductStockClient> {
        @Override
        public ProductStockClient create(Throwable throwable) {
            log.error("触发降级处理,e={}", throwable.getMessage());
            return new ProductStockClient() {
                @Override
                public Result<?> createStockPreReview(StockItemDto stockItemDto) {
                    return Result.error(throwable.getMessage());
                }
                
            };
        }
    }
}
