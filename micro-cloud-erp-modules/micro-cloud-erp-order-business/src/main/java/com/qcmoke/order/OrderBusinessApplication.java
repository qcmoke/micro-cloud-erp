package com.qcmoke.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.qcmoke.common.service"})
public class OrderBusinessApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderBusinessApplication.class, args);
    }
}
