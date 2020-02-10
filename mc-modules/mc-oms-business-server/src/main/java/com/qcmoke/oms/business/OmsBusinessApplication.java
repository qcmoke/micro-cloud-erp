package com.qcmoke.oms.business;

import com.qcmoke.common.annotation.EnableAutoResourceServerSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author qcmoke
 */
@EnableAutoResourceServerSecurityConfig
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class OmsBusinessApplication {
    public static void main(String[] args) {
        SpringApplication.run(OmsBusinessApplication.class, args);
    }
}
