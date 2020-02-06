package com.qcmoke.ums;

import com.qcmoke.common.annotation.EnableAutoSecurityTokenInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@EnableAutoSecurityTokenInterceptor
@EnableEurekaClient//Eureka客户端
@SpringBootApplication
public class UmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(UmsApplication.class, args);
    }
}
