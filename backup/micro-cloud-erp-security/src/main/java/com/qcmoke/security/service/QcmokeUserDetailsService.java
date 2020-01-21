package com.qcmoke.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 扩展UserDetailsService接口
 * UserDetailsServices实现基于用户名和密码的用户登录
 */
public interface QcmokeUserDetailsService extends UserDetailsService {

    /**
     * 扩展手机验证码登录
     *
     * @param mobile
     * @return
     */
    UserDetails loadUserByMobile(String mobile) throws UsernameNotFoundException;
}