package com.qcmoke.gateway.config;

import com.qcmoke.auth.common.handler.PermissionExpressionHandler;
import com.qcmoke.auth.common.handler.SecurityOauth2AccessDeniedHandler;
import com.qcmoke.auth.common.handler.SecurityOauth2AuthenticationEntryPointHandler;
import com.qcmoke.gateway.authorization.CustomMetadataSource;
import com.qcmoke.gateway.authorization.UrlAccessDecisionManager;
import com.qcmoke.gateway.constant.RouteConstant;
import com.qcmoke.gateway.filter.ZuulLogFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.util.AntPathMatcher;

/**
 * @author qcmoke
 */
@Slf4j
@Configuration
@EnableResourceServer
public class ZuulResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private CustomMetadataSource metadataSource;
    @Autowired
    private UrlAccessDecisionManager urlAccessDecisionManager;
    @Autowired
    private ZuulLogFilter zuulLogFilter;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //设置免认证白名单(不经过SecurityInterceptor，直接通过)
        http.requestMatcher(new NegatedRequestMatcher(request -> antPathMatcher.match(RouteConstant.OAUTH_GATEWAY_ROUTE_URL, request.getRequestURI())));
        http.addFilterBefore(zuulLogFilter, FilterSecurityInterceptor.class);
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        //给请求url赋予对应的角色字符串，但不做是否匹配的判断
                        o.setSecurityMetadataSource(metadataSource);
                        //判断url和角色是否匹配
                        o.setAccessDecisionManager(urlAccessDecisionManager);
                        return o;
                    }
                })
                //通过当前的请求和当前的用户得到请求是否授权,使用GatewayWebSecurityExpressionHandler表达式处理器去解析这个表达式
                .anyRequest().access("#permissionService.notAllowedAnonymousUser(request,authentication)")
                .anyRequest().authenticated();
    }


    //@Autowired
    //private DefaultTokenServices defaultTokenServices;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //resources.tokenServices(defaultTokenServices);
        resources
                .authenticationEntryPoint(new SecurityOauth2AuthenticationEntryPointHandler())
                .accessDeniedHandler(new SecurityOauth2AccessDeniedHandler())
                //支持表达式#permissionService.hasPermission(request,authentication)
                .expressionHandler(new PermissionExpressionHandler());
    }
}
