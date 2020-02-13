package com.qcmoke.auth.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author qcmoke
 */
@Data
@Component
@ConfigurationProperties(prefix = "oauth2.social")
public class Oauth2SocialProperties {
    private String frontUrl;
    private String clientId;
    private String socialUserPassword;
}
