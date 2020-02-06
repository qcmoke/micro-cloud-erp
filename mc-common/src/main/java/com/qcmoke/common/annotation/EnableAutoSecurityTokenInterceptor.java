package com.qcmoke.common.annotation;

import com.qcmoke.common.config.SecurityTokenInterceptorConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SecurityTokenInterceptorConfig.class)
public @interface EnableAutoSecurityTokenInterceptor {
}
