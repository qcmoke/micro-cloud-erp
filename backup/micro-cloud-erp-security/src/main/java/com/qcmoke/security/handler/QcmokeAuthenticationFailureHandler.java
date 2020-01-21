package com.qcmoke.security.handler;

import com.qcmoke.core.utils.RespBean;
import com.qcmoke.core.utils.ResponseWriterUtil;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理器
 */
@Component
public class QcmokeAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        RespBean respBean = null;
        if (e instanceof BadCredentialsException || e instanceof UsernameNotFoundException) {
            respBean = RespBean.unauthorized("账户名或者密码输入错误!");
        } else if (e instanceof LockedException) {
            respBean = RespBean.unauthorized("账户被锁定，请联系管理员!");
        } else if (e instanceof CredentialsExpiredException) {
            respBean = RespBean.unauthorized("密码过期，请联系管理员!");
        } else if (e instanceof AccountExpiredException) {
            respBean = RespBean.unauthorized("账户过期，请联系管理员!");
        } else if (e instanceof DisabledException) {
            respBean = RespBean.unauthorized("账户被禁用，请联系管理员!");
        } else {
            respBean = RespBean.unauthorized(e.getMessage());
        }
        ResponseWriterUtil.writeJson(respBean);
    }
}
