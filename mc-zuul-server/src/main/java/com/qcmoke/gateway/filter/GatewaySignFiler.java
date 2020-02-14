package com.qcmoke.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.qcmoke.common.utils.security.RSAUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.SignatureException;
import java.util.UUID;

/**
 * 网关签名过滤器
 *
 * @author qcmoke
 */
@Slf4j
@Component
public class GatewaySignFiler extends ZuulFilter {

    /**
     * 签名密私钥
     */
    private static final String GATEWAY_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALHnnHQIbW2l0V7CC+45/hzcMsUIaUKlvIBIgmFfmkvWj9wa5Fx/f+dPmwSlwZw83ZxSd3nsEHDyHn8fjjbauIXqETvXN6sYqexHaVYCaBmZNyCFEYuCz7XEqnqxQVC20lJBwhL3byI3pUmaUKQXvQdPZhr+f+t8XzKbHwrHtR2VAgMBAAECgYAaykMAIijAY0EFIPmE9Uyz8eDfVOXs+GJLex/PJANrOjNNtOsAlt6e6ZjxeTiPm4bPvIdrX8YWDA/Vmt3imstAdsstcFHGGgRryaSVkLtklHoCOirEMJR84SIwx7coro8uRcJVY+lo6r40uCq/4naAa11Wif49DYsMsNmNiCRNgQJBAOYXqrh1V/E4HgTxgkwJeEBbb8CoCSDGxnzxT0wsflqXMJ1VmSVNJ/eV2F7WIg9cK0umJBaFKEvGXVx9faQg6bUCQQDF76be6mq2fTLGViAzX+tODUuLp+/q9B55eVwcvJ21s3HS5GRI0AMeILtvvC+rFrn8LoW/hOqtBxI0sJYl5FBhAkEA2a2QPHv/G90cQU7+FtNqqXAXtGsEX7bN90wP2h/J1ghs3Jwri2eIJSnlDiuFA4UODL58K7YD3lQm5SZvo8PjdQJAadyZyAFh34YoYNFxWWjEpbMQo3nHJEc6AUf6DtiGFMcLanqCdDrkX/mrpb/lUsDN6eVL3TmOdcohX5LOSyfIIQJBANQwNmTer7NnwgQLD1utxgaNHXcrNEdpj3OLUiXcupbLWS1dgCysKmrSJYUsuSFGjnDV5uMYUCU1+uJSuJSkJXQ=";

    /**
     * 是否启用该网关过滤器
     *
     * @return boolean
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }


    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        //签名内容
        String gatewaySignContent = UUID.randomUUID().toString();
        //网关签名结果
        String gatewaySignResult = null;
        try {
            gatewaySignResult = RSAUtil.rsaSign(gatewaySignContent, GATEWAY_PRIVATE_KEY, StandardCharsets.UTF_8.name());
        } catch (SignatureException e) {
            log.error("网关签名异常,e=" + e.getMessage());
            throw new ZuulException(e, HttpStatus.INTERNAL_SERVER_ERROR.value(), "网关签名异常");
        }
        ctx.addZuulRequestHeader("gatewaySignContent", gatewaySignContent);
        ctx.addZuulRequestHeader("gatewaySignResult", gatewaySignResult);
        return null;
    }
}