package com.qcmoke.auth;

import com.qcmoke.common.annotation.EnableAutoRedisService;
import com.qcmoke.common.annotation.EnableAutoResourceServerSecurity;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @author qcmoke
 */
@EnableAutoResourceServerSecurity
@EnableAutoRedisService
@EnableDiscoveryClient
@MapperScan("com.qcmoke.auth.mapper")
@SpringBootApplication
public class AuthServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
