package com.qcmoke.oms;

import com.qcmoke.common.annotation.EnableAutoResourceServerSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author qcmoke
 */
@EnableAutoResourceServerSecurityConfig
@EnableDiscoveryClient
@SpringBootApplication
public class OmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(OmsApplication.class, args);
    }
}
