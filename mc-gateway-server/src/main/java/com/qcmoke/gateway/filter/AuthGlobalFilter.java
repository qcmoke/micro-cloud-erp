package com.qcmoke.gateway.filter;

import com.qcmoke.common.dto.CurrentUser;
import com.qcmoke.common.service.PublicKeyService;
import com.qcmoke.common.utils.JwtRsaUtils;
import com.qcmoke.common.utils.OauthSecurityJwtUtil;
import com.qcmoke.common.utils.OauthSecurityUtil;
import com.qcmoke.common.vo.Result;
import com.qcmoke.gateway.authorization.CustomMetadataSource;
import com.qcmoke.gateway.authorization.UrlAccessDecisionManager;
import com.qcmoke.gateway.constant.RouteConstant;
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
@Order(0)
@Component
public class AuthGlobalFilter implements GlobalFilter {
    @Autowired
    private PublicKeyService publicKeyService;
    @Autowired
    private CustomMetadataSource customMetadataSource;
    @Autowired
    private UrlAccessDecisionManager urlAccessDecisionManager;
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        if (antPathMatcher.match(RouteConstant.OAUTH_ROUTE_URL, path)) {
            return chain.filter(exchange);
        }
        PublicKey publicKey = publicKeyService.getPublicKey();
        if (publicKey == null) {
            return ResponseWriterUtil.makeResponse(response, Result.error("服务器异常，无法获取公钥"));
        }
        String bearerToken = OauthSecurityUtil.getBearerTokenForWebFlux(request);
        if (StringUtils.isBlank(bearerToken)) {
            return ResponseWriterUtil.makeResponse(response, Result.error("未认证，无访问令牌"));
        }
        try {
            JwtRsaUtils.parserToken(bearerToken, publicKey);
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
        Collection<String> urlNeedRoles = customMetadataSource.getAttributes(path);
        CurrentUser currentUser = OauthSecurityJwtUtil.getCurrentUserForWebFlux(request);
        boolean decide = urlAccessDecisionManager.decide(currentUser, urlNeedRoles);
        if (!decide) {
            return ResponseWriterUtil.makeResponse(response, Result.error("权限不足!"));
        }
        return chain.filter(exchange);
    }
}