package com.qcmoke.gateway.config;

import com.qcmoke.common.handler.PermissionExpressionHandler;
import com.qcmoke.common.handler.SecurityOAuth2AccessDeniedHandler;
import com.qcmoke.common.handler.SecurityOAuth2AuthenticationEntryPointHandler;
import com.qcmoke.gateway.authorization.CustomMetadataSource;
import com.qcmoke.gateway.authorization.UrlAccessDecisionManager;
import com.qcmoke.gateway.filter.GatewayAuthLogFilter;
import com.qcmoke.gateway.properties.GatewayAuthProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

@Configuration
@EnableResourceServer
public class GatewayResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private CustomMetadataSource metadataSource;
    @Autowired
    private UrlAccessDecisionManager urlAccessDecisionManager;
    @Autowired
    private GatewayAuthProperties gatewayAuthProperties;
    @Autowired
    private GatewayAuthLogFilter gatewayAuthLogFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(gatewayAuthLogFilter, FilterSecurityInterceptor.class);
        http.authorizeRequests()
                .antMatchers(StringUtils.split(gatewayAuthProperties.getIgnoreAuthenticateUrl(), ",")).permitAll()
                .anyRequest().authenticated()
                .anyRequest().access("#permissionService.notAllowedAnonymousUser(request,authentication)")//通过当前的请求和当前的用户得到请求是否授权,使用GatewayWebSecurityExpressionHandler表达式处理器去解析这个表达式
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(metadataSource);//给请求url赋予对应的角色字符串，但不做是否匹配的判断
                        o.setAccessDecisionManager(urlAccessDecisionManager);//判断url和角色是否匹配
                        return o;
                    }
                });
    }


//    @Autowired
//    private DefaultTokenServices defaultTokenServices;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        resources.tokenServices(defaultTokenServices);
        resources
                .authenticationEntryPoint(new SecurityOAuth2AuthenticationEntryPointHandler())
                .accessDeniedHandler(new SecurityOAuth2AccessDeniedHandler())
                .expressionHandler(new PermissionExpressionHandler());//支持表达式#permissionService.hasPermission(request,authentication)
    }
}
