package com.qcmoke.auth;

import com.qcmoke.common.annotation.EnableAutoRedisService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @author qcmoke
 */
@EnableAutoRedisService
@EnableDiscoveryClient
@SpringBootApplication
public class Oauth2AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(Oauth2AuthServerApplication.class, args);
    }

}
