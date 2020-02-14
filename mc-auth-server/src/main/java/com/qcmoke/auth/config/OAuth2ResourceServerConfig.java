package com.qcmoke.auth.config;

import com.qcmoke.auth.common.handler.PermissionExpressionHandler;
import com.qcmoke.auth.common.handler.SecurityOauth2AccessDeniedHandler;
import com.qcmoke.auth.common.handler.SecurityOauth2AuthenticationEntryPointHandler;
import com.qcmoke.auth.constant.AuthUrlConstant;
import com.qcmoke.auth.properties.Oauth2SecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/**
 * 资源服务器配置
 * @author qcmoke
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
        //以/resource开头的所有请求都被认证服务器控制
        http.antMatcher(AuthUrlConstant.ALL_RESOURCE_URL);
        http.authorizeRequests()
                .antMatchers(StringUtils.splitByWholeSeparatorPreserveAllTokens(oauth2SecurityProperties.getAnonUrl(), ",")).permitAll()
                .anyRequest().authenticated()
                .anyRequest().access("#permissionService.notAllowedAnonymousUser(request,authentication)");
        http.csrf().disable();
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.authenticationEntryPoint(new SecurityOauth2AuthenticationEntryPointHandler())
                .accessDeniedHandler(new SecurityOauth2AccessDeniedHandler())
                .expressionHandler(new PermissionExpressionHandler());
    }
}
