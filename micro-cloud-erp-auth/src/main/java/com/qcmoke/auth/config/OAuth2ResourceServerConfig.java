package com.qcmoke.auth.config;

import com.qcmoke.auth.constant.AuthUrlConstant;
import com.qcmoke.auth.properties.Oauth2SecurityProperties;
import com.qcmoke.common.handler.PermissionExpressionHandler;
import com.qcmoke.common.handler.SecurityOAuth2AccessDeniedHandler;
import com.qcmoke.common.handler.SecurityOAuth2AuthenticationEntryPointHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 资源服务器配置
 */
@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private Oauth2SecurityProperties oauth2SecurityProperties;

    /**
     * 资源服务器的过滤规则
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher(AuthUrlConstant.ALL_RESOURCE_URL);//以/resource开头的所有请求都被认证服务器控制
        http.authorizeRequests()
                .antMatchers(StringUtils.splitByWholeSeparatorPreserveAllTokens(oauth2SecurityProperties.getAnonUrl(), ",")).permitAll()
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
