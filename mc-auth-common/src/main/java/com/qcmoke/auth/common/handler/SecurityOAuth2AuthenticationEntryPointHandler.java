package com.qcmoke.auth.common.handler;

import com.qcmoke.auth.common.exception.NotAllowedAnonymousUserException;
import com.qcmoke.common.utils.ResponseWriterUtil;
import com.qcmoke.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权异常处理-->处理匿名token(不传token)用户或者非法token用户访问无权限资源时的异常
 */
@Slf4j
public class SecurityOAuth2AuthenticationEntryPointHandler extends OAuth2AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        String msg;
        if (e instanceof NotAllowedAnonymousUserException) {//AccessTokenRequiredException
            msg = "请求中无token，不支持匿名授权，auth fail 401！e=" + e.getMessage();
            log.error(msg);
            ResponseWriterUtil.writeJson(Result.unauthorized(msg));
            return;
        }
        msg = "token非法，auth fail 401！e=" + e.getMessage();
        log.error(msg);
        ResponseWriterUtil.writeJson(Result.unauthorized(msg));
    }
}
