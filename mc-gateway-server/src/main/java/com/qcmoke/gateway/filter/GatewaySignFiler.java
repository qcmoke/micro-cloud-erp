package com.qcmoke.gateway.filter;

import com.qcmoke.common.vo.Result;
import com.qcmoke.common.utils.security.RSAUtil;
import com.qcmoke.gateway.utils.ResponseWriterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.UUID;

/**
 * 网关签名过滤器
 *
 * @author qcmoke
 */
@Order(0)
@Component
@Slf4j
public class GatewaySignFiler implements GlobalFilter {
    /**
     * 签名私钥
     */
    private static final String GATEWAY_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALHnnHQIbW2l0V7CC+45/hzcMsUIaUKlvIBIgmFfmkvWj9wa5Fx/f+dPmwSlwZw83ZxSd3nsEHDyHn8fjjbauIXqETvXN6sYqexHaVYCaBmZNyCFEYuCz7XEqnqxQVC20lJBwhL3byI3pUmaUKQXvQdPZhr+f+t8XzKbHwrHtR2VAgMBAAECgYAaykMAIijAY0EFIPmE9Uyz8eDfVOXs+GJLex/PJANrOjNNtOsAlt6e6ZjxeTiPm4bPvIdrX8YWDA/Vmt3imstAdsstcFHGGgRryaSVkLtklHoCOirEMJR84SIwx7coro8uRcJVY+lo6r40uCq/4naAa11Wif49DYsMsNmNiCRNgQJBAOYXqrh1V/E4HgTxgkwJeEBbb8CoCSDGxnzxT0wsflqXMJ1VmSVNJ/eV2F7WIg9cK0umJBaFKEvGXVx9faQg6bUCQQDF76be6mq2fTLGViAzX+tODUuLp+/q9B55eVwcvJ21s3HS5GRI0AMeILtvvC+rFrn8LoW/hOqtBxI0sJYl5FBhAkEA2a2QPHv/G90cQU7+FtNqqXAXtGsEX7bN90wP2h/J1ghs3Jwri2eIJSnlDiuFA4UODL58K7YD3lQm5SZvo8PjdQJAadyZyAFh34YoYNFxWWjEpbMQo3nHJEc6AUf6DtiGFMcLanqCdDrkX/mrpb/lUsDN6eVL3TmOdcohX5LOSyfIIQJBANQwNmTer7NnwgQLD1utxgaNHXcrNEdpj3OLUiXcupbLWS1dgCysKmrSJYUsuSFGjnDV5uMYUCU1+uJSuJSkJXQ=";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        return doFilterInternal(request, response, exchange, chain);
    }

    protected Mono<Void> doFilterInternal(ServerHttpRequest request, ServerHttpResponse response, ServerWebExchange exchange, GatewayFilterChain chain) {
        //签名内容
        String gatewaySignContent = UUID.randomUUID().toString();
        //网关签名结果
        String gatewaySignResult = null;
        try {
            //为了安全性，后期可以设置签名过期时间，防止网关签名一直有效
            gatewaySignResult = RSAUtil.rsaSign(gatewaySignContent, GATEWAY_PRIVATE_KEY, StandardCharsets.UTF_8.name());
        } catch (SignatureException e) {
            log.error("网关签名异常,e=" + e.getMessage());
            return ResponseWriterUtil.makeResponse(response, Result.error("网关签名异常"));
        }
        ServerHttpRequest build = exchange.getRequest().mutate()
                .header("gatewaySignContent", gatewaySignContent)
                .header("gatewaySignResult", gatewaySignResult)
                .build();
        ServerWebExchange newExchange = exchange.mutate().request(build).build();
        return chain.filter(newExchange);
    }
}
