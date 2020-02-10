package com.qcmoke.common.annotation;

import com.qcmoke.common.config.CheckGatewaySignInterceptorMvcConfig;
import com.qcmoke.common.config.FeignTokenInterceptorConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 资源服务器安全自动配置
 * 1、必须通过网关访问资源服务器
 * 2、Feign安全传递
 *
 * @author qcmoke
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        //验签网关请求 Interceptor拦截器
        CheckGatewaySignInterceptorMvcConfig.class,
        //Feign调用时添加http head请求头
        FeignTokenInterceptorConfig.class})
public @interface EnableAutoResourceServerSecurityConfig {
}
