package com.qcmoke.zuul.filter;

import com.alibaba.fastjson.JSONObject;
import com.qcmoke.common.utils.OAuthSecurityJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 日志过滤器
 */
@Component
@Slf4j
public class AuthLogFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //认证成功后授权前
        JSONObject jwt = OAuthSecurityJwtUtil.getPrincipal(request);
        if (jwt == null) {
            log("匿名用户访问");
            chain.doFilter(request, response);
            return;
        }
        String username = (String) jwt.get("username");
        log(username + "开始进行授权");
        //授权中
        chain.doFilter(request, response);
        //授权成功后
        log(username + "完成授权");
    }

    private void log(String sql) {
        log.info(sql);
    }
}
