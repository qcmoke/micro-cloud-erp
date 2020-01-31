package com.qcmoke.auth;

import com.qcmoke.common.annotation.EnableAutoRedisService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@EnableAutoRedisService
@SpringBootApplication
public class OAuth2AuthServer {

    public static void main(String[] args) {
        SpringApplication.run(OAuth2AuthServer.class, args);
    }

}
