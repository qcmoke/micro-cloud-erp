package com.qcmoke.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.qcmoke.core.entity.TokenInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * 认证拦截过滤器
 * 用户是否认证都通过这个过滤器，让审计日志拦截过滤器记录后，最后让授权拦截过滤器来处理拦截
 */
@Slf4j
@Component
public class OAuth2Filter extends ZuulFilter {

    private RestTemplate restTemplate = new RestTemplate();

    /**
     * 是否启用该网关过滤器
     *
     * @return boolean
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器链顺序（数字越小过滤器越排在前面）
     *
     * @return int
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 网关过滤器类型
     * 有四种类型：pre(执行前)、post(执行后)、error(抛出异常后)、route(执行路由转发)
     *
     * @return String
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 校验token
     * 过滤器执行的处理业务
     *
     * @return 返回null则结束处理并放行
     * @throws ZuulException ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        HttpServletRequest request = RequestContext.getCurrentContext().getRequest();
        //发往认证服务器的请求不检验token，去认证服务器进行认证的请求直接放行
        if (StringUtils.startsWith(request.getRequestURI(), "/token")) {
            return null;
        }
        try {
            TokenInfo tokenInfo = getTokenInfo(request);
            if (tokenInfo != null) {
                request.setAttribute("tokenInfo", tokenInfo);
            }
        } catch (Exception e) {
            log.error("get token info fail,e={}", e.getMessage());
        }
        return null;
    }

    /**
     * 到认证服务器校验token，并获取token信息
     *
     * @param request HttpServletRequest
     * @return TokenInfo
     */
    private TokenInfo getTokenInfo(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.isBlank(authHeader)) {
            return null;
        }
        if (!StringUtils.startsWithIgnoreCase(authHeader, "bearer ")) {
            return null;
        }
        String token = StringUtils.substringAfter(authHeader, "bearer ");
        String oauthServiceUrl = "http://localhost:9090/oauth/check_token";

        HttpHeaders headers = new HttpHeaders(){{
            setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            set("Authorization", getAuthorizationHeader("gateway", "123456"));
        }};
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>(){{
            add("token", token);
        }};

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        ResponseEntity<TokenInfo> response = restTemplate.exchange(
                oauthServiceUrl,
                HttpMethod.POST,
                entity,
                TokenInfo.class);//将返回结果转化成TokenInfo

        return response.getBody();
    }


    private String getAuthorizationHeader(String clientId, String clientSecret) {
        if (clientId == null || clientSecret == null) {
            log.warn("Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
        }
        String creds = String.format("%s:%s", clientId, clientSecret);
        return "Basic " + new String(Base64.encode(creds.getBytes(StandardCharsets.UTF_8)));
    }
}
