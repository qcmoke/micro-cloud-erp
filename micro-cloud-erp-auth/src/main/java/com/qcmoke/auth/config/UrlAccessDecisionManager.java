package com.qcmoke.auth.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 判断url和角色是否匹配
 * 当前一个请求走完 FilterlnvocationSecurityMetadataSource 中的getAttributes 方法后，接下来就会AccessDecisionManager 类中进行角色信息的 比对，
 * Created by sang on 2017/12/28.
 */
@Component
public class UrlAccessDecisionManager implements AccessDecisionManager {

    /**
     * 判断当前登录的用户是否具备当前请求 URL 所需要的角色信息。
     * 如果不具备，就抛出 AccessDeniedException 异常，否则不做任何事即可
     * <p>
     * 这里涉及到一个all和any的问题：假设当前用户具备角色A、角色B，当前请求需要角色B、角色C，那么是要当前用户要包含所有请求角色才算授权成功还是只要包含一个就算授权成功？我这里采用了第二种方案，即只要用户有一个请求所需要的角色即可。小伙伴可根据自己的实际情况调整decide方法中的逻辑。
     *
     * @param auth 当前登录用户的信息
     * @param o    Filterlnvocation 对象
     * @param cas  当前请求 URL需要的角色列表，FilterlnvocationSecurityMetadataSource 中的 getAttributes 方法的返回值
     */
    @Override
    public void decide(Authentication auth, Object o, Collection<ConfigAttribute> cas) {
        if (auth instanceof AnonymousAuthenticationToken) {//如果为访问的是匿名用户时（为登录认证就像访问授权资源时）
//            throw new BadCredentialsException("访问此资源需要身份验证");
            throw new InsufficientAuthenticationException("访问此资源需要身份验证");
        }
        for (ConfigAttribute ca : cas) {
            //当前请求需要的角色
            String needRole = ca.getAttribute();
            if ("ROLE_LOGIN".equals(needRole)) {
                return;
            }
            //当前用户所具有的角色列表
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                //判断请求url需要的角色与用户所具有的角色相等的话即可通过
                if (authority.getAuthority().equals(needRole)) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("权限不足!");
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}