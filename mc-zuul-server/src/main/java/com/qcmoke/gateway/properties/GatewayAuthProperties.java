package com.qcmoke.gateway.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "gateway.security")
public class GatewayAuthProperties {

    /**
     * 免认证访问路径
     */
    private String ignoreAuthenticateUrl;

    /**
     * 免授权的路径
     */
    private String ignoreAuthorizationUrl;

}
