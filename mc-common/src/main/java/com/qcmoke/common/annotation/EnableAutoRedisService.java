package com.qcmoke.common.annotation;


import com.qcmoke.common.config.RedisServiceConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisServiceConfig.class)
public @interface EnableAutoRedisService {

}