package com.qcmoke.auth.properties;

import com.qcmoke.auth.dto.SocialSourceDto;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qcmoke
 */
@Data
@Component
@ConfigurationProperties(prefix = "oauth2.social")
public class Oauth2SocialProperties {
    private String frontUrl;
    private String clientId;
    private List<SocialSourceDto> sourceList;
}
