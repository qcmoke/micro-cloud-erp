package com.qcmoke.gateway.filter;

import com.alibaba.fastjson.JSONObject;
import com.qcmoke.common.service.PublicKeyService;
import com.qcmoke.common.utils.OauthSecurityUtil;
import com.qcmoke.common.vo.Result;
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
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器(对所有路由都起作用)
 *
 * @author qcmoke
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {
    @Autowired
    private PublicKeyService publicKeyService;

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
        String publicKey = publicKeyService.getPublicKey();
        String accessToken = OauthSecurityUtil.getAccessTokenForWebFlux(request);
        return chain.filter(exchange);
    }


    private Mono<Void> makeResponse(ServerHttpResponse response, Result<Object> result) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        DataBuffer dataBuffer = response.bufferFactory().wrap(JSONObject.toJSONString(result).getBytes());
        return response.writeWith(Mono.just(dataBuffer));
    }
}
