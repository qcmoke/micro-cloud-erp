package com.qcmoke.gateway.filter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 日志过滤器
 */
public class GatewayAuditLogFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //认证成功后授权前
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        excuteSql(String.format("insert into log(username,create_date) values('%s','%s')", username, new Date().toLocaleString()));

        //授权中
        chain.doFilter(request, response);

        //授权成功后
        if (StringUtils.isNoneBlank((String) request.getAttribute("logAuthFailMsg"))) {
            excuteSql(String.format("update log set status = '%s' where username = '%s'", "success", username));
        }
    }

    private void excuteSql(String sql) {
        System.out.println(sql);
    }
}
