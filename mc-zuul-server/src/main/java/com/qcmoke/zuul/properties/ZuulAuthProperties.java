package com.qcmoke.zuul.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "gateway.security")
public class ZuulAuthProperties {

    /**
     * 免认证访问路径
     */
    private String ignoreAuthenticateUrl;

    /**
     * 免授权的路径
     */
    private String ignoreAuthorizationUrl;

}
