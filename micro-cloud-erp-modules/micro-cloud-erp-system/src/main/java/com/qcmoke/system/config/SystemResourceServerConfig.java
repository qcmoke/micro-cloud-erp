package com.qcmoke.system.config;

import com.qcmoke.common.handler.PermissionExpressionHandler;
import com.qcmoke.common.handler.SecurityOAuth2AccessDeniedHandler;
import com.qcmoke.common.handler.SecurityOAuth2AuthenticationEntryPointHandler;
import com.qcmoke.system.converter.CustomerAccessTokenConverter;
import com.qcmoke.system.utils.GatewayUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.io.IOException;

@Configuration
@EnableResourceServer
public class SystemResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    CustomerAccessTokenConverter customerAccessTokenConverter;
    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated();
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(tokenServices());
    }


    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        Resource resource = new ClassPathResource("key/public.key");
        String publicKey = null;
        try {
            publicKey = GatewayUtil.inputStream2String(resource.getInputStream());
        } catch (final IOException e) {
            throw new RuntimeException("读取公钥文件异常");
        }
        converter.setVerifierKey(publicKey);
        converter.setAccessTokenConverter(customerAccessTokenConverter);
        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

}
