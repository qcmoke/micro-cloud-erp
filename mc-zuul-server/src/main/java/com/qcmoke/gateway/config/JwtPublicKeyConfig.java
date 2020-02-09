package com.qcmoke.gateway.config;//package com.qcmoke.gateway.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
///**
// * 需要在ResourceServerConfig里做如下配置
// *
// * @Autowired
// * private DefaultTokenServices defaultTokenServices;
// * public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
// *      ....
// *      ....
// *      ....
// *      resources.tokenServices(tokenServices());
// * }
// */
//@Configuration
//public class JwtPublicKeyConfig {
//
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
//            publicKey = inputStream2String(resource.getInputStream());
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
//
//    public static String inputStream2String(InputStream is) throws IOException {
//        BufferedReader in = new BufferedReader(new InputStreamReader(is));
//        StringBuilder buffer = new StringBuilder();
//        String line = "";
//        while ((line = in.readLine()) != null) {
//            buffer.append(line);
//        }
//        return buffer.toString();
//    }
//}
