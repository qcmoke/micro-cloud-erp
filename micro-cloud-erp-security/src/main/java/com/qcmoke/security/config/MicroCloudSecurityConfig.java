package com.qcmoke.security.config;

import com.qcmoke.security.constant.SecurityConstants;
import com.qcmoke.security.filter.ValidateCodeFilter;
import com.qcmoke.security.handler.QcmokeAuthenticationFailureHandler;
import com.qcmoke.security.handler.QcmokeAuthenticationSuccessHandler;
import com.qcmoke.security.service.QcmokeUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * 浏览器环境下安全配置主类
 * 1、支持用户名+密码+验证码登录
 * 2、支持手机号+手机短信验证码登录
 */
@Configuration
public class MicroCloudSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource;
    @Autowired
    private QcmokeAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private QcmokeAuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private QcmokeUserDetailsService userDetailsService;

    @Autowired
    private ValidateCodeFilter validateCodeFilter;
    @Autowired
    private SmsAuthenticationConfig smsAuthenticationConfig;

    /**
     * 认证配置
     *
     * @param http HttpSecurity
     * @throws Exception 认证异常
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class);

        //登录表单相关配置
        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .usernameParameter(SecurityConstants.DEFAULT_PARAMETER_USERNAME_LOGIN).passwordParameter(SecurityConstants.DEFAULT_PARAMETER_PASSWORD_LOGIN)
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler);
        //记住我相关配置
        http.rememberMe()
                .tokenRepository(this.persistentTokenRepository())
                .tokenValiditySeconds(SecurityConstants.REMEMBER_ME_SECONDS)
                //.rememberMeParameter("rememberMe")
                .userDetailsService(userDetailsService);
        //放行配置
        http.authorizeRequests()
                .antMatchers(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        SecurityConstants.LOGIN_PAGE,
                        SecurityConstants.SIGN_UP_URL,
                        SecurityConstants.USER_REGIST_API)
                .permitAll()
                .anyRequest().authenticated();

        //引入短信验证码配置
        http.apply(smsAuthenticationConfig);

        //关闭CSRF默认配置
        http.csrf().disable();
    }


    /**
     * 配置记住我相关配置
     *
     * @return PersistentTokenRepository
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        //启动时创建记住我先关的数据库表,如果数据库已经有了同名的表，则创建失败，应该在第一次创建表时才开启该选项
        //jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }


    /**
     * 配置用户认证加密方式，使用spring security自带的BCryptPasswordEncoder实现方式。
     *
     * @return PasswordEncoder
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        //如果不加密密码(比如数据库明文存储用户密码)可以使用NoOpPasswordEncoder.getInstance();
        return new BCryptPasswordEncoder();
    }

    /**
     * 认证配置
     *
     * @param auth AuthenticationManagerBuilder
     * @throws Exception 异常
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*
         * 1.通过hrService设置从数据库获取用户数据并设置UserDetails
         * 2.设置登录时用户密码的加密方案，要求：登录时用户密码的加密方案 和 注册（org.sang.service.HrService#hrReg(java.lang.String, java.lang.String)）时用户密码的加密方案一致
         *(其实发现省略这两项也可以)
         */
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(this.passwordEncoder());
    }
}