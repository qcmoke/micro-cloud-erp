package com.qcmoke.ums;

import com.qcmoke.common.annotation.EnableAutoResourceServerSecurity;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author qcmoke
 */
@EnableAutoResourceServerSecurity
@EnableDiscoveryClient
@ComponentScan({ "com.qcmoke.ums","com.qcmoke.common.handler"})
@MapperScan("com.qcmoke.ums.mapper")
@SpringBootApplication
public class UmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(UmsApplication.class, args);
    }
}
