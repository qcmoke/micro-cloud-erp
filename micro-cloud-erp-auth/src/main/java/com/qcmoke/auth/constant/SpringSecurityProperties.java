package com.qcmoke.auth.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityProperties {

    private static String[] ignoreUris;

    @Value("${security.ignore-uris}")
    public void setIgnoreUri(String uris) {
        ignoreUris = uris.split(",");
    }

    public static String[] getIgnoreUris() {
        return ignoreUris;
    }

}
