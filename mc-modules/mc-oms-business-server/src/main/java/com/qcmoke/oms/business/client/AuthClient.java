package com.qcmoke.oms.business.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author qcmoke
 */
@FeignClient(value = "mc-auth-server")
public interface AuthClient {
    @GetMapping("/resource/user")
    Object currentUser();
}
