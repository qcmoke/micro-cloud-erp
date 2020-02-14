package com.qcmoke.gateway.filter;

import com.qcmoke.common.dto.CurrentUser;
import com.qcmoke.common.dto.Result;
import com.qcmoke.common.service.PublicKeyService;
import com.qcmoke.common.utils.JwtRsaUtil;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.utils.oauth.OauthSecurityUtil;
import com.qcmoke.gateway.authorization.CustomMetadataSource;
import com.qcmoke.gateway.authorization.UrlAccessDecisionManager;
import com.qcmoke.gateway.constant.RouteConstant;
import com.qcmoke.gateway.utils.GatewayContextUtil;
import com.qcmoke.gateway.utils.ResponseWriterUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.util.Collection;

/**
 * 全局过滤器(对所有路由都起作用)
 *
 * @author qcmoke
 */
@Slf4j
@Order(1)
@Component
public class AuthGlobalFilter implements GlobalFilter {
    @Autowired
    private PublicKeyService publicKeyService;
    @Autowired
    private CustomMetadataSource customMetadataSource;
    @Autowired
    private UrlAccessDecisionManager urlAccessDecisionManager;
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String originalPath = GatewayContextUtil.getOriginalPath(exchange);
        log.info("客户端原始请求路径 originalPath={}", originalPath);
        if (antPathMatcher.match(RouteConstant.OAUTH_GATEWAY_ROUTE_URL, originalPath)) {
            log.info("request is auth server....");
            return chain.filter(exchange);
        }
        log.info("check token and user permission....");
        PublicKey publicKey = null;
        try {
            publicKey = publicKeyService.getPublicKey();
        } catch (Exception e) {
            return ResponseWriterUtil.makeResponse(response, Result.error("服务器异常，无法获取公钥"));
        }
        if (publicKey == null) {
            return ResponseWriterUtil.makeResponse(response, Result.error("服务器异常，无法获取公钥"));
        }
        String bearerToken = OauthSecurityUtil.getBearerTokenForWebFlux(request);
        if (StringUtils.isBlank(bearerToken)) {
            return ResponseWriterUtil.makeResponse(response, Result.error("未认证，无访问令牌"));
        }
        try {
            JwtRsaUtil.parserAndVerifyToken(bearerToken, publicKey);
        } catch (ExpiredJwtException e) {
            return ResponseWriterUtil.makeResponse(response, Result.error("令牌已失效,e=" + e.getMessage()));
        } catch (UnsupportedJwtException e) {
            return ResponseWriterUtil.makeResponse(response, Result.error("不支持该类型的令牌,e=" + e.getMessage()));
        } catch (MalformedJwtException e) {
            return ResponseWriterUtil.makeResponse(response, Result.error("令牌格式错误,e=" + e.getMessage()));
        } catch (SignatureException e) {
            return ResponseWriterUtil.makeResponse(response, Result.error("令牌签名异常,e=" + e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseWriterUtil.makeResponse(response, Result.error("令牌签名证书非法,e=" + e.getMessage()));
        } catch (Exception e) {
            return ResponseWriterUtil.makeResponse(response, Result.error("令牌验证失败,e=" + e.getMessage()));
        }
        Collection<String> urlNeedRoles = customMetadataSource.getAttributes(originalPath);
        CurrentUser currentUser = OauthSecurityJwtUtil.getCurrentUserForWebFlux(request);
        boolean decide = urlAccessDecisionManager.decide(currentUser, urlNeedRoles);
        if (!decide) {
            return ResponseWriterUtil.makeResponse(response, Result.error("权限不足!"));
        }
        return chain.filter(exchange);
    }
}