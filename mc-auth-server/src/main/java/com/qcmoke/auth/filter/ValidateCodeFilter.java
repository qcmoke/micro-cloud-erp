package com.qcmoke.auth.filter;

import com.qcmoke.auth.constant.GrantTypeConstant;
import com.qcmoke.auth.constant.AuthUrlConstant;
import com.qcmoke.auth.constant.ParamsConstant;
import com.qcmoke.auth.exception.ValidateCodeException;
import com.qcmoke.auth.properties.Oauth2SecurityProperties;
import com.qcmoke.auth.service.ValidateCodeService;
import com.qcmoke.common.vo.Result;
import com.qcmoke.common.utils.ResponseWriterUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 验证码过滤器
 * 给oauth2密码认证模式添加验证码
 */
@Slf4j
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {

    @Autowired
    private ValidateCodeService validateCodeService;
    @Autowired
    private Oauth2SecurityProperties oauth2SecurityProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String clientId = getClientId(header);
        RequestMatcher matcher = new AntPathRequestMatcher(AuthUrlConstant.OAUTH_TOKEN_URL, HttpMethod.POST.toString());
        if (matcher.matches(request)
                && StringUtils.equalsIgnoreCase(request.getParameter(ParamsConstant.GRANT_TYPE), GrantTypeConstant.PASSWORD)
                && !StringUtils.equalsAnyIgnoreCase(clientId, StringUtils.split(oauth2SecurityProperties.getIgnoreValidateCodeClientIds(), ","))) {
            try {
                validateCode(request);
                chain.doFilter(request, response);
            } catch (ValidateCodeException e) {
                log.error(e.getMessage());
                ResponseWriterUtil.writeJson(Result.error(e.getMessage()));
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private void validateCode(HttpServletRequest httpServletRequest) throws ValidateCodeException {
        String code = httpServletRequest.getParameter(ParamsConstant.VALIDATE_CODE_CODE);
        String key = httpServletRequest.getParameter(ParamsConstant.VALIDATE_CODE_KEY);
        validateCodeService.check(key, code);
    }

    private String getClientId(String header) {
        String clientId = "";
        try {
            byte[] base64Token = header.substring(6).getBytes(StandardCharsets.UTF_8);
            byte[] decoded;
            decoded = Base64.getDecoder().decode(base64Token);
            String token = new String(decoded, StandardCharsets.UTF_8);
            int delim = token.indexOf(":");
            if (delim != -1) {
                clientId = new String[]{token.substring(0, delim), token.substring(delim + 1)}[0];
            }
        } catch (Exception ignore) {
        }
        return clientId;
    }
}
