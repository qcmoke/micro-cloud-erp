package com.qcmoke.fms;

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
@ComponentScan({"com.qcmoke.fms", "com.qcmoke.common.handler"})
@MapperScan("com.qcmoke.fms.mapper")
@SpringBootApplication
public class FmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(FmsApplication.class, args);
    }
}
