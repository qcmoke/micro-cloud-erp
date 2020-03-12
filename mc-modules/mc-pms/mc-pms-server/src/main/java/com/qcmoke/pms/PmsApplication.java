package com.qcmoke.pms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author qcmoke
 */
//@EnableAutoResourceServerSecurity
@EnableDiscoveryClient
@ComponentScan({"com.qcmoke.pms", "com.qcmoke.common.handler"})
@MapperScan("com.qcmoke.pms.mapper")
@SpringBootApplication
public class PmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(PmsApplication.class, args);
    }
}
