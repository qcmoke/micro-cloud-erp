package com.qcmoke.security.config;

import com.qcmoke.security.filter.SmsCodeAuthenticationFilter;
import com.qcmoke.security.provider.SmsCodeAuthenticationProvider;
import com.qcmoke.security.service.QcmokeUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 手机验证码处理器（移动端和pc端都能使用）
 *
 * @author wenxiaohua
 * @version v1.0
 * @description demo
 * @date 2019年11月13日 17:27
 */
@Component
public class SmsAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private QcmokeUserDetailsService qcmokeUserDetailsService;

    private SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
    private SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter();

    @Override
    public void configure(HttpSecurity http) throws Exception {
        /**
         * 初始化SmsCodeAuthenticationProvider和SmsCodeAuthenticationFilter
         */
        smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        smsCodeAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        smsCodeAuthenticationProvider.setUserDetailsService(qcmokeUserDetailsService);

        http
                .authenticationProvider(smsCodeAuthenticationProvider)
                .addFilterAfter(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    }
}