package com.qcmoke.common.annotation;

import com.qcmoke.common.config.FeignTokenInterceptorConfig;
import com.qcmoke.common.config.CheckGatewaySignInterceptorMvcConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author qcmoke
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({
        //Feign调用时添加http head请求头
        FeignTokenInterceptorConfig.class,
        //验签网关请求 Interceptor拦截器
        CheckGatewaySignInterceptorMvcConfig.class})
public @interface EnableAutoCommonConfig {
}
