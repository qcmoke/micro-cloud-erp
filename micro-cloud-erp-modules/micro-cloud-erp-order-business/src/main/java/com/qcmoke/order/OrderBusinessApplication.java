package com.qcmoke.order;

import com.qcmoke.common.annotation.EnableAutoFeignTokenInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableAutoFeignTokenInterceptor//让Feign携带http head数据
@EnableFeignClients//启用Feign
@EnableEurekaClient//Eureka客户端
@EnableResourceServer//认证服务器
@SpringBootApplication
public class OrderBusinessApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderBusinessApplication.class, args);
    }
}
