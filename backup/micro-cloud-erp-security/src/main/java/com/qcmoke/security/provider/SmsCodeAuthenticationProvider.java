package com.qcmoke.security.provider;

import com.qcmoke.security.service.QcmokeUserDetailsService;
import com.qcmoke.security.token.SmsCodeAuthenticationToken;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 手机短信+验证码认证处理器
 */
@Setter
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private QcmokeUserDetailsService userDetailsService;

    /**
     * 通过手机号查询用户信息最后封装到到“经过认证”的SmsCodeAuthenticationToken对象
     *
     * @param authentication 手机号SmsCodeAuthenticationToken
     * @return 认证后得到的用户信息SmsCodeAuthenticationToken
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        String mobile = (String) authenticationToken.getPrincipal();
        UserDetails user = userDetailsService.loadUserByMobile(mobile);
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法通过mobile:" + mobile + "获取用户信息");
        }
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    /**
     * 让SmsCodeAuthenticationProvider只支持认证SmsCodeAuthenticationToken
     *
     * @param authentication
     * @return 是否支持
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
