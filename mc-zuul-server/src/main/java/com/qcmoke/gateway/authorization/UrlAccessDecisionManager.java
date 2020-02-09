package com.qcmoke.gateway.authorization;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;


/**
 * 授权控制
 * （进行最终的访问控制(授权)决策,判断url和角色是否匹配）
 * 当前一个请求走完 FilterlnvocationSecurityMetadataSource 中的getAttributes 方法后，接下来就会AccessDecisionManager 类中进行角色信息的 比对，
 * @author qcmoke
 */
@Component
public class UrlAccessDecisionManager implements AccessDecisionManager {

    /**
     * 自定义授权策略
     * 比对当前登录的用户的角色与当前请求URL所需要的角色。只要用户中有一个角色包含在前请求URL所需要的角色里，那么直接return结束，否则就抛出 AccessDeniedException异常。
     * （这里涉及到一个all和any的问题：假设当前用户具备角色A、角色B，当前请求需要角色B、角色C，那么是要当前用户要包含所有请求角色才算授权成功还是只要包含一个就算授权成功？我这里采用了第二种方案，即只要用户有一个请求所需要的角色即可。小伙伴可根据自己的实际情况调整decide方法中的逻辑。）
     *
     * @param currentUser  当前登录用户的信息
     * @param o            Filterlnvocation 对象
     * @param urlNeedRoles 当前请求 URL需要的角色列表,CustomMetadataSource中的 getAttributes 方法的返回值
     */
    @Override
    public void decide(Authentication currentUser, Object o, Collection<ConfigAttribute> urlNeedRoles) {
        for (ConfigAttribute attribute : urlNeedRoles) {
            //当前请求需要的角色
            String needRole = attribute.getAttribute();
            if ("ROLE_LOGIN".equals(needRole)) {
                if (currentUser instanceof AnonymousAuthenticationToken) {
                    throw new BadCredentialsException("未认证");
                } else {
                    return;
                }
            }
            //当前用户所具有的角色列表
            Collection<? extends GrantedAuthority> authorities = currentUser.getAuthorities();
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