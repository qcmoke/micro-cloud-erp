package com.qcmoke.common.config;

import com.qcmoke.common.interceptor.CheckGatewaySignInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 添加“验签网关请求拦截器”到mvc
 * 在mvc里可以直接注入
 *
 * @author qcmoke
 */
@Slf4j
public class CheckGatewaySignInterceptorMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckGatewaySignInterceptor());
    }
}
