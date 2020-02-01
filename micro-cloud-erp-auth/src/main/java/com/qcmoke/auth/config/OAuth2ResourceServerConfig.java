package com.qcmoke.auth.config;

import com.qcmoke.auth.properties.Oauth2SecurityProperties;
import com.qcmoke.common.handler.PermissionExpressionHandler;
import com.qcmoke.common.handler.SecurityOAuth2AccessDeniedHandler;
import com.qcmoke.common.handler.SecurityOAuth2AuthenticationEntryPointHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 资源服务器配置
 */
@Slf4j
@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private Oauth2SecurityProperties oauth2SecurityProperties;

    /**
     * 资源服务器的顾虑规则
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        String[] anonUrl = StringUtils.splitByWholeSeparatorPreserveAllTokens(oauth2SecurityProperties.getAnonUrl(), ",");
        List<String> apis = new ArrayList<>();
        apis.add("/api/**");
        apis.addAll(Arrays.asList(anonUrl));
        String[] resources = apis.toArray(new String[0]);
        log.info("resources={}", (Object) resources);
        http.requestMatchers().antMatchers(resources);
        http.authorizeRequests()
                .antMatchers(anonUrl).permitAll()
                .anyRequest().authenticated()
                .anyRequest().access("#permissionService.notAllowedAnonymousUser(request,authentication)");
        http.csrf().disable();
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(new SecurityOAuth2AuthenticationEntryPointHandler())
                .accessDeniedHandler(new SecurityOAuth2AccessDeniedHandler())
                .expressionHandler(new PermissionExpressionHandler());
    }
}
