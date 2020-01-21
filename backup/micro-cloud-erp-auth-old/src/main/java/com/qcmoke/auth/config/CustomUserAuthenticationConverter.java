package com.qcmoke.auth.config;

import com.qcmoke.auth.entity.User;
import com.qcmoke.auth.service.SysUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {
    @Autowired
    SysUserDetailsService userDetailsService;

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        Object principal = authentication.getPrincipal();
        User user;
        if (principal instanceof User) {
            user = (User) principal;
        } else {
            //refresh_token默认不去调用userdetailService获取用户信息，这里我们手动去调用，得到 UserJwt
            String name = authentication.getName();
            UserDetails userDetails = userDetailsService.loadUserByUsername(name);
            user = (User) userDetails;
        }
        response.put("name", user.getUsername());
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        if (!CollectionUtils.isEmpty(authorities)) {
            response.put("authorities", AuthorityUtils.authorityListToSet(authorities));
        }

        return response;
    }


}
