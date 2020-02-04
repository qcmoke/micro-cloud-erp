package com.qcmoke.system;

import com.qcmoke.common.annotation.EnableAutoSecurityTokenInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;


@EnableAutoSecurityTokenInterceptor
@EnableResourceServer
@SpringBootApplication
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class, args);
    }
}
