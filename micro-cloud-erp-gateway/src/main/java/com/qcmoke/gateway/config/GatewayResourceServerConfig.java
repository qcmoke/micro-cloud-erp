package com.qcmoke.gateway.config;

import com.qcmoke.gateway.filter.GatewayAuditLogFilter;
import com.qcmoke.gateway.handler.GatewayAccessDeniedHandler;
import com.qcmoke.gateway.handler.GatewayAuthenticationEntryPointHandler;
import com.qcmoke.gateway.handler.GatewayWebSecurityExpressionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;

@Configuration
@EnableResourceServer
public class GatewayResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private GatewayWebSecurityExpressionHandler gatewayWebSecurityExpressionHandler;
    @Autowired
    private GatewayAccessDeniedHandler gatewayAccessDeniedHandler;
    @Autowired
    private GatewayAuthenticationEntryPointHandler gatewayAuthenticationEntryPointHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //日志过滤器(在认证之后，授权之前)
        http.addFilterBefore(new GatewayAuditLogFilter(), ExceptionTranslationFilter.class);

        http.authorizeRequests()
                .antMatchers("/token/**").permitAll()
                .anyRequest().access("#permissionService.hasPermission(request,authentication)");//通过当前的请求和当前的用户得到请求是否授权,使用GatewayWebSecurityExpressionHandler表达式处理器去解析这个表达式
        //.anyRequest().authenticated();//所有请求都需要认证
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                .authenticationEntryPoint(gatewayAuthenticationEntryPointHandler)
                .accessDeniedHandler(gatewayAccessDeniedHandler)
                .expressionHandler(gatewayWebSecurityExpressionHandler);
    }
}
