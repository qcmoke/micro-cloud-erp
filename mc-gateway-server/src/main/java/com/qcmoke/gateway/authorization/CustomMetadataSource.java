package com.qcmoke.gateway.authorization;


import com.qcmoke.gateway.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author qcmoke
 */
@Component
public class CustomMetadataSource {

    @Autowired
    private MenuService menuService;
    AntPathMatcher antPathMatcher = new AntPathMatcher();


    public Collection<String> getAttributes(String requestUrl) {
        Map<String, Set<String>> metadataMap = menuService.getMetadataMap();
        for (Map.Entry<String, Set<String>> metadataDto : metadataMap.entrySet()) {
            String acl = metadataDto.getKey();
            if (antPathMatcher.match(acl, requestUrl)) {
                return metadataDto.getValue();
            }
        }
        return Collections.singleton("ROLE_LOGIN");
    }

}
