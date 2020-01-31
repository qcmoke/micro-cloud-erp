package com.qcmoke.system.converter;

import com.qcmoke.system.service.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CustomerAccessTokenConverter extends DefaultAccessTokenConverter {
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public CustomerAccessTokenConverter() {
        super.setUserTokenConverter(new CustomerUserAuthenticationConverter());
    }


    private class CustomerUserAuthenticationConverter extends DefaultUserAuthenticationConverter {


        public Authentication extractAuthentication(Map<String, ?> map) {
            UserDetails userDetails = userDetailsService.loadUserByUsername((String) map.get("user_name"));
            return new UsernamePasswordAuthenticationToken(userDetails, "N/A", userDetails.getAuthorities());
        }
    }

}