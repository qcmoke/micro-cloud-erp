package com.qcmoke.auth.config;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qcmoke.auth.properties.Oauth2SecurityProperties;
import com.qcmoke.common.utils.OauthSecurityJwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.UUID;

@Configuration
@EnableAuthorizationServer
public class Oauth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DataSource dataSource;
    @Qualifier("userDetailsServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private Oauth2SecurityProperties oauth2SecurityProperties;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /**
     * 配置认证服务器
     * 1、token保存的地方
     * 2、认证管理器
     *
     * @param endpoints endpoints
     * @throws Exception Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                //设置token关联的用户信息
                .userDetailsService(userDetailsService)
                //配置保存token的地方，比如：数据库、redis、jwt里
                .tokenStore(tokenStore())
                //认证管理器
                .authenticationManager(authenticationManager);
        //.exceptionTranslator(new UserOAuth2WebResponseExceptionTranslator());//错误处理翻译器
        if (oauth2SecurityProperties.getEnableJwt()) {
            //endpoints.accessTokenConverter(jwtAccessTokenConverter());
            endpoints.tokenEnhancer(jwtAccessTokenConverter());
        }
    }


    /**
     * 配置认证客户端
     *
     * @param clients clients
     * @throws Exception Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    /**
     * 配置认证方式
     *
     * @param security security
     * @throws Exception Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("isAuthenticated()")
                .checkTokenAccess("isAuthenticated()");
    }


    /**
     * token保存方式
     *
     * @return TokenStore
     */
    @Bean
    public TokenStore tokenStore() {
        if (oauth2SecurityProperties.getEnableJwt()) {
            return new JwtTokenStore(jwtAccessTokenConverter());
        } else {
            CustomRedisTokenStore redisTokenStore = new CustomRedisTokenStore(redisConnectionFactory);
            // 解决每次生成的 token都一样的问题
            redisTokenStore.setAuthenticationKeyGenerator(oAuth2Authentication -> UUID.randomUUID().toString());
            return redisTokenStore;
        }
    }


    /**
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {

        JwtAccessTokenConverter converter = new JwtAccessTokenConverter() {
            //可以重写该方法对token进行增强
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication authentication) {
                Object principal = authentication.getPrincipal();
                JSONObject user = JSONObject.parseObject(JSONObject.toJSONString(principal, SerializerFeature.WriteMapNullValue));//WriteMapNullValue空值的属性要保留，默认移除空值属性
                user.remove(OauthSecurityJwtUtil.JWT_USER_AUTHORITIES_PROPERTY);
                //设置附加信息
                ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(new HashMap<String, Object>() {{
                    put(OauthSecurityJwtUtil.JWT_USER_PROPERTY, user);
                }});
                return super.enhance(oAuth2AccessToken, authentication);
            }
        };

        /*使用秘钥签名（以下两种可选）*/
        //(1)使用对称加密签名
        //accessTokenConverter.setSigningKey("123456");
        //(2)使用非对称加密证书文件进行签名
        //(2.1)使用keystore
        KeyProperties keyProperties = keyProperties();
        converter.setKeyPair(new KeyStoreKeyFactory(keyProperties.getKeyStore().getLocation(), keyProperties.getKeyStore().getPassword().toCharArray())
                .getKeyPair(keyProperties.getKeyStore().getAlias(), keyProperties.getKeyStore().getSecret().toCharArray())
        );
        //(2.2)使用自定义的Rsa密钥对
       /* PrivateKey privateKey = null;
        PublicKey publicKey = null;
        try {
            privateKey = RsaUtils.getPrivateKey(new ClassPathResource("key/private.key").getFile().getAbsolutePath());
            publicKey = RsaUtils.getPublicKey(new ClassPathResource("key/public.key").getFile().getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        KeyPair keyPair = RsaUtils.getKeyPair(privateKey, publicKey);
        converter.setKeyPair(keyPair);*/
        return converter;
    }

    @Bean
    @ConfigurationProperties(prefix = "encrypt")
    public KeyProperties keyProperties() {
        return new KeyProperties();
    }
}