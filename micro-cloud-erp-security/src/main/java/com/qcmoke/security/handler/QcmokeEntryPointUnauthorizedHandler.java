package com.qcmoke.security.handler;

import com.qcmoke.core.utils.RespBean;
import com.qcmoke.core.utils.ResponseWriterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用来解决匿名用户访问无权限资源时的异常
 */
@Component
@Slf4j
public class QcmokeEntryPointUnauthorizedHandler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        log.error("className:{},msg:{}", e.getClass().getName(), e.getMessage());
        ResponseWriterUtil.writeJson(RespBean.unauthorized("请登录认证完成再请求该接口！"));
    }
}
