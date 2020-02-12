package com.qcmoke.gateway;


import com.qcmoke.common.annotation.EnableAutoRedisService;
import com.qcmoke.common.service.PublicKeyService;
import com.qcmoke.common.service.impl.PublicKeyServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.HashMap;


/**
 * @author qcmoke
 */
@Slf4j
@EnableAutoRedisService
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayApplication {

    @Bean
    @ConfigurationProperties(prefix = "jwt-oauth-service-public-key")
    public HashMap<String, String> jwtPublicKey() {
        return new HashMap<>();
    }

    @Autowired
    private HashMap<String, String> jwtPublicKey;

    @Bean
    public PublicKeyService publicKeyService() throws Exception {
        PublicKeyServiceImpl publicKeyService = new PublicKeyServiceImpl(
                jwtPublicKey.get("oauth-service-ip"),
                jwtPublicKey.get("oauth-service-port"),
                jwtPublicKey.get("client-id"),
                jwtPublicKey.get("client-secret"));
        String publicKey = publicKeyService.getPublicPemKey();
        log.info("publicKey={}", publicKey);
        return publicKeyService;
    }

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}