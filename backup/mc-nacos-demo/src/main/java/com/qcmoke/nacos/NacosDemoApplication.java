package com.qcmoke.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient
@SpringBootApplication
public class NacosDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosDemoApplication.class, args);
    }
}
