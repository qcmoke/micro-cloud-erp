package com.qcmoke.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.qcmoke.common.dto.CurrentUser;
import com.qcmoke.common.service.PublicKeyService;
import com.qcmoke.common.utils.OauthSecurityJwtUtil;
import com.qcmoke.common.utils.OauthSecurityUtil;
import com.qcmoke.common.vo.Result;
import com.qcmoke.gateway.authorization.CustomMetadataSource;
import com.qcmoke.gateway.authorization.UrlAccessDecisionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;

/**
 * 全局过滤器(对所有路由都起作用)
 *
 * @author qcmoke
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Autowired
    private PublicKeyService publicKeyService;

    @Autowired
    private CustomMetadataSource customMetadataSource;

    @Autowired
    private UrlAccessDecisionManager urlAccessDecisionManager;

    AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 过滤器执行顺序
     */
    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();

        if (antPathMatcher.match("/auth/**", path)) {
            return chain.filter(exchange);
        }

        String publicKey = publicKeyService.getPublicKey();
        String accessToken = OauthSecurityUtil.getAccessTokenForWebFlux(request);
        boolean isOk = OauthSecurityJwtUtil.checkToken(accessToken, publicKey);
        if (!isOk) {
            return makeResponse(response, Result.error("未认证"));
        }
        Collection<String> urlNeedRoles = customMetadataSource.getAttributes(path);
        CurrentUser currentUser = OauthSecurityJwtUtil.getCurrentUserForWebFlux(request);
        boolean decide = urlAccessDecisionManager.decide(currentUser, urlNeedRoles);
        if (!decide) {
            return makeResponse(response, Result.error("权限不足!"));
        }
        return chain.filter(exchange);
    }


    private Mono<Void> makeResponse(ServerHttpResponse response, Result<Object> result) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSONObject.toJSONString(result).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }
}
