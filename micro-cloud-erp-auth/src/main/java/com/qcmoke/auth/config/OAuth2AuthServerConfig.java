package com.qcmoke.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }


    /**
     * 配置认证服务器
     * 1、认证管理器
     * 2、token保存的地方
     *
     * @param endpoints endpoints
     * @throws Exception Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //.tokenStore(tokenStore()) //通过数据源进行校验
                .authenticationManager(authenticationManager);
    }

    /**
     * 配置认证客户端
     * clientId: （必须的）客户端 id
     * secret: （要求用于受信任的客户端）客户端的机密，如果有的话
     * scope: 客户范围限制。如果范围未定义或为空（默认），客户端将不受范围限制
     * authorizedGrantTypes: 授权客户端使用的授予类型。默认值为空
     * authorities: 授权给客户的认证（常规 Spring Security 认证）
     *
     * @param clients clients
     * @throws Exception Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        tokenStoreInMemory(clients);
        //clients.jdbc(dataSource);
    }

    /**
     * 配置认证方式
     *
     * @param security security
     * @throws Exception Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("isAuthenticated()");
    }

    /**
     * token存在内存中
     *
     * @param clients clients
     * @throws Exception Exception
     */
    private void tokenStoreInMemory(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("orderApp")//客户端id
                .secret(passwordEncoder.encode("123456"))//客户认证秘钥
                .accessTokenValiditySeconds(3600)//token有效期
                .scopes("read", "write")//客户范围限制
                .resourceIds("order-server")//客户端需要访问的资源服务器id
                .authorizedGrantTypes("password")//授权模式

                .and()

                .withClient("orderServer")
                .secret(passwordEncoder.encode("123456"))
                .accessTokenValiditySeconds(3600)
                .scopes("read")
                .resourceIds("order-server")
                .authorizedGrantTypes("password");
    }
}
