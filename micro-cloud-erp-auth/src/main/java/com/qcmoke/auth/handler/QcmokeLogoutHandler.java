package com.qcmoke.auth.handler;

import com.qcmoke.auth.utils.JwtUtil;
import com.qcmoke.core.utils.RespBean;
import com.qcmoke.core.utils.ResponseWriterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class QcmokeLogoutHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Authentication token = JwtUtil.getAuthentication(httpServletRequest.getHeader(JwtUtil.REQUEST_TOKEN_NAME));
        if (token != null) {
            Object principal = token.getPrincipal();
            log.info("用户：{}注销成功", principal);
        }

        ResponseWriterUtil.writeJson(RespBean.ok("注销成功!"));
    }
}
