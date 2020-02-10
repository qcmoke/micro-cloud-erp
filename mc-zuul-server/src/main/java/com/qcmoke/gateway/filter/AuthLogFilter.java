package com.qcmoke.gateway.filter;

import com.qcmoke.common.utils.OauthSecurityJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 日志过滤器
 *
 * @author qcmoke
 */
@Component
@Slf4j
public class AuthLogFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        //认证成功后授权前
        String currentUsername = OauthSecurityJwtUtil.getCurrentUsername(request);
        if (StringUtils.isBlank(currentUsername)) {
            log("匿名用户访问");
            chain.doFilter(request, response);
            return;
        }
        log(currentUsername + "开始进行授权");
        //授权中
        chain.doFilter(request, response);
        //授权成功后
        log(currentUsername + "完成授权");
    }

    private void log(String sql) {
        log.info(sql);
    }
}
