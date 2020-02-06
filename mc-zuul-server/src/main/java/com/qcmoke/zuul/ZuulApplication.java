package com.qcmoke.zuul;

import com.qcmoke.common.annotation.EnableAutoRedisService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableAutoRedisService
@EnableZuulProxy
@EnableDiscoveryClient
//@EnableEurekaClient//Eureka客户端
@SpringBootApplication
public class ZuulApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulApplication.class, args);
    }
}