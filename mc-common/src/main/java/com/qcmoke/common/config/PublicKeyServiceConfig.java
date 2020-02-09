package com.qcmoke.common.config;

import com.qcmoke.common.service.PublicKeyService;
import com.qcmoke.common.service.impl.PublicKeyServiceImpl;
import org.springframework.context.annotation.Bean;

/**
 * @author qcmoke
 */
public class PublicKeyServiceConfig {
    @Bean
    public PublicKeyService publicKeyService() {
        return new PublicKeyServiceImpl();
    }
}
