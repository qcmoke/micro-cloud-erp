package com.qcmoke.common.handler;

import com.qcmoke.common.exception.NotAllowedAnonymousUserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.http.HttpServletRequest;

/**
 * 安全表达式处理器
 * 让#permissionService.hasPermission(request,authentication)起作用
 */
public class PermissionExpressionHandler extends OAuth2WebSecurityExpressionHandler {

    @Override
    protected StandardEvaluationContext createEvaluationContextInternal(Authentication authentication, FilterInvocation invocation) {
        StandardEvaluationContext sec = super.createEvaluationContextInternal(authentication, invocation);
        sec.setVariable("permissionService", new PermissionService());
        return sec;
    }

    /**
     * 必须包含token才能授权,AnonymousAuthenticationToken是不传token的情况，springsecurity默认会创建一个anonymousUser的用户，如果token是错误的不会到达此处验证授权，在认证时就会抛出异常
     */
    @Slf4j
    private static class PermissionService {
        public boolean notAllowedAnonymousUser(HttpServletRequest request, Authentication authentication) {
            if (authentication instanceof AnonymousAuthenticationToken) {
                throw new NotAllowedAnonymousUserException("不支持匿名用户授权资源");
            }
            return true;
        }

//        public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
//            log.info("uri={}", request.getRequestURI());
//            log.info("authentication={}", ReflectionToStringBuilder.toString(authentication));
//
//            //下面可以改成从数据库获取用户权限并进行权限校验
//            /*boolean flag = RandomUtils.nextInt() % 2 == 0;
//            if (!flag) {
//                log.info("访问未授权");
//            }*/
//
//            return true;
//        }
    }

}
