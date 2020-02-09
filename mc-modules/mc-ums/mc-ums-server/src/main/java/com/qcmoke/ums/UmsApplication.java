package com.qcmoke.ums;

import com.qcmoke.common.annotation.EnableAutoCommonConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @author qcmoke
 */
@EnableAutoCommonConfig
@EnableDiscoveryClient
@SpringBootApplication
public class UmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(UmsApplication.class, args);
    }
}
