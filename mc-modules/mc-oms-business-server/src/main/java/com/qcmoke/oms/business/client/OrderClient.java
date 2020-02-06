package com.qcmoke.oms.business.client;

import com.qcmoke.common.constant.GlobalServiceListConstant;
import com.qcmoke.oms.api.OrderApi;
import com.qcmoke.oms.business.client.fallback.OrderClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = GlobalServiceListConstant.MC_OMS_SERVER_ID, fallbackFactory = OrderClientFallbackFactory.class)
public interface OrderClient extends OrderApi {
}
