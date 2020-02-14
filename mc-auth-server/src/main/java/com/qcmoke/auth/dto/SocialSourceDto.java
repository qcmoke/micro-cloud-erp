package com.qcmoke.auth.dto;

import lombok.Data;

/**
 * @author qcmoke
 */
@Data
public class SocialSourceDto {
    private String socialType;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
}
