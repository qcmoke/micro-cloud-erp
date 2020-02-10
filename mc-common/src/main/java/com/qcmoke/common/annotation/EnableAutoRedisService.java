package com.qcmoke.common.annotation;


import com.qcmoke.common.config.RedisServiceConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自动配置RedisService，使其生效。（依赖redis数据源）
 *
 * @author qcmoke
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisServiceConfig.class)
public @interface EnableAutoRedisService {

}