package com.qcmoke.wms;

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
@EnableFeignClients
@EnableDiscoveryClient
@EnableAutoResourceServerSecurity
@ComponentScan({"com.qcmoke.wms", "com.qcmoke.common.handler"})
@MapperScan("com.qcmoke.wms.mapper")
@SpringBootApplication
public class WmsApplication {
    public static void main(String[] args) {
        SpringApplication.run(WmsApplication.class, args);
    }
}
