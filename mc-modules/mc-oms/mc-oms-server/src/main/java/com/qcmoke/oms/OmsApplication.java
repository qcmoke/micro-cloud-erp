package com.qcmoke.oms;

import com.qcmoke.common.annotation.EnableAutoResourceServerSecurity;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author qcmoke
 */
@EnableAutoResourceServerSecurity
@EnableFeignClients
@EnableDiscoveryClient
@ComponentScan({"com.qcmoke.oms", "com.qcmoke.common.handler"})
@MapperScan("com.qcmoke.oms.mapper")
@SpringBootApplication
public class OmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(OmsApplication.class, args);
    }
}
