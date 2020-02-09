package com.qcmoke.common.annotation;

import com.qcmoke.common.config.PublicKeyServiceConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自动装配从认证服务器得到公钥（需要认证服务器提前启动）
 *
 * @author qcmoke
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(PublicKeyServiceConfig.class)
public @interface EnableAutoPublicKeyService {
}
