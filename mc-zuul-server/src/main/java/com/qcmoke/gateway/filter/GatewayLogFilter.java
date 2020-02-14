package com.qcmoke.gateway.filter;

import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.gateway.constant.RouteConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
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
public class GatewayLogFilter extends OncePerRequestFilter {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        if (antPathMatcher.match(RouteConstant.OAUTH_GATEWAY_ROUTE_URL, requestUri)) {
            log("用户对认证服务器访问,requestURI:" + requestUri);
            chain.doFilter(request, response);
            return;
        }

        //认证成功后授权前
        String currentUsername = OauthSecurityJwtUtil.getCurrentUsername(request);
        if (StringUtils.isBlank(currentUsername)) {
            log("匿名用户访问,requestURI:" + requestUri);
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
