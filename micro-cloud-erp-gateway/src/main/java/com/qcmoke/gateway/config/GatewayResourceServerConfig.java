package com.qcmoke.gateway.config;

import com.qcmoke.common.handler.PermissionExpressionHandler;
import com.qcmoke.common.handler.SecurityOAuth2AccessDeniedHandler;
import com.qcmoke.common.handler.SecurityOAuth2AuthenticationEntryPointHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class GatewayResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {


        http.authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
                .anyRequest().access("#permissionService.notAllowedAnonymousUser(request,authentication)");
                //.anyRequest().access("#permissionService.hasPermission(request,authentication)");//通过当前的请求和当前的用户得到请求是否授权,使用GatewayWebSecurityExpressionHandler表达式处理器去解析这个表达式
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //resources.tokenServices(tokenServices());
        resources
                .authenticationEntryPoint(new SecurityOAuth2AuthenticationEntryPointHandler())
                .accessDeniedHandler(new SecurityOAuth2AccessDeniedHandler())
                .expressionHandler(new PermissionExpressionHandler());//支持表达式#permissionService.hasPermission(request,authentication)
    }


//    @Bean
//    public TokenStore tokenStore() {
//        return new JwtTokenStore(accessTokenConverter());
//    }
//
//    @Bean
//    public JwtAccessTokenConverter accessTokenConverter() {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        Resource resource = new ClassPathResource("key/public.key");
//        String publicKey = null;
//        try {
//            publicKey = GatewayUtil.inputStream2String(resource.getInputStream());
//        } catch (final IOException e) {
//            throw new RuntimeException("读取公钥文件异常");
//        }
//        converter.setVerifierKey(publicKey);
//        //converter.setAccessTokenConverter(new CustomerAccessTokenConverter());
//        return converter;
//    }
//
//    @Bean
//    @Primary
//    public DefaultTokenServices tokenServices() {
//        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
//        defaultTokenServices.setTokenStore(tokenStore());
//        return defaultTokenServices;
//    }
}
