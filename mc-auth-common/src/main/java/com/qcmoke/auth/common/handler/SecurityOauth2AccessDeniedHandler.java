package com.qcmoke.auth.common.handler;

import com.qcmoke.common.utils.ResponseWriterUtil;
import com.qcmoke.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 授权异常处理-->处理认证成功的用户访问无权限资源时的异常
 *
 * @author qcmoke
 */
@Slf4j
public class SecurityOauth2AccessDeniedHandler extends OAuth2AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        String msg = "权限不足，授权失败! auth fail 403! e=" + e.getMessage();
        log.error(msg);
        ResponseWriterUtil.writeJson(Result.forbidden(msg));
    }
}
