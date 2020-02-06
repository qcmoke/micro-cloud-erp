package com.qcmoke.oms.business;

import com.qcmoke.common.annotation.EnableAutoFeignTokenInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableAutoFeignTokenInterceptor//让Feign携带http head数据
@EnableFeignClients//启用Feign
@EnableDiscoveryClient
//@EnableEurekaClient//Eureka客户端
@SpringBootApplication
public class OmsBusinessApplication {
    public static void main(String[] args) {
        SpringApplication.run(OmsBusinessApplication.class, args);
    }
}
