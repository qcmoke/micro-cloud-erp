package com.qcmoke.common.annotation;

import com.qcmoke.common.config.FeignTokenInterceptorConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(FeignTokenInterceptorConfig.class)
public @interface EnableAutoFeignTokenInterceptor {
}
