package com.qcmoke.gateway.filter;

import com.qcmoke.common.utils.OauthSecurityUtil;
import com.qcmoke.common.utils.ResponseWriterUtil;
import com.qcmoke.common.utils.security.RSAUtils;
import com.qcmoke.common.vo.Result;
import com.qcmoke.gateway.wrapper.HeaderMapRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;

/**
 * 网关签名过滤器
 * <p>
 * 对Basic token或者Bearer token进行签名
 * 要求所有请求都要携带Bearer Token或者Basic Token
 *
 * @author qcmoke
 */
@Component
@Slf4j
public class AuthGatewaySignFiler extends OncePerRequestFilter {
    /**
     * 签名密私钥
     */
    private static final String GATEWAY_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALHnnHQIbW2l0V7CC+45/hzcMsUIaUKlvIBIgmFfmkvWj9wa5Fx/f+dPmwSlwZw83ZxSd3nsEHDyHn8fjjbauIXqETvXN6sYqexHaVYCaBmZNyCFEYuCz7XEqnqxQVC20lJBwhL3byI3pUmaUKQXvQdPZhr+f+t8XzKbHwrHtR2VAgMBAAECgYAaykMAIijAY0EFIPmE9Uyz8eDfVOXs+GJLex/PJANrOjNNtOsAlt6e6ZjxeTiPm4bPvIdrX8YWDA/Vmt3imstAdsstcFHGGgRryaSVkLtklHoCOirEMJR84SIwx7coro8uRcJVY+lo6r40uCq/4naAa11Wif49DYsMsNmNiCRNgQJBAOYXqrh1V/E4HgTxgkwJeEBbb8CoCSDGxnzxT0wsflqXMJ1VmSVNJ/eV2F7WIg9cK0umJBaFKEvGXVx9faQg6bUCQQDF76be6mq2fTLGViAzX+tODUuLp+/q9B55eVwcvJ21s3HS5GRI0AMeILtvvC+rFrn8LoW/hOqtBxI0sJYl5FBhAkEA2a2QPHv/G90cQU7+FtNqqXAXtGsEX7bN90wP2h/J1ghs3Jwri2eIJSnlDiuFA4UODL58K7YD3lQm5SZvo8PjdQJAadyZyAFh34YoYNFxWWjEpbMQo3nHJEc6AUf6DtiGFMcLanqCdDrkX/mrpb/lUsDN6eVL3TmOdcohX5LOSyfIIQJBANQwNmTer7NnwgQLD1utxgaNHXcrNEdpj3OLUiXcupbLWS1dgCysKmrSJYUsuSFGjnDV5uMYUCU1+uJSuJSkJXQ=";

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain chain) throws ServletException, IOException {

        String bearerToken = OauthSecurityUtil.getBearerToken(request);
        String basicToken = OauthSecurityUtil.getBasicToken(request);

        if (StringUtils.isBlank(basicToken) && StringUtils.isBlank(bearerToken)) {
            String errorMsg = "网关签名异常,请求认证服务器既无Basic Token，也无Bearer Token";
            log.error(errorMsg);
            ResponseWriterUtil.writeJson(Result.error(errorMsg));
            return;
        }

        //签名内容
        String content = null;
        if (StringUtils.isNoneBlank(basicToken)) {
            content = basicToken;
        } else {
            content = bearerToken;
        }

        //网关签名结果内容
        String gatewaySign = null;
        try {
            //对accessToken进行签名
            gatewaySign = RSAUtils.rsaSign(content, GATEWAY_PRIVATE_KEY, StandardCharsets.UTF_8.name());
        } catch (SignatureException e) {
            log.error("网关签名异常,e={}", e.getMessage());
            ResponseWriterUtil.writeJson(Result.error("网关签名异常"));
            return;
        }
        HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
        requestWrapper.addHeader("gatewaySign", gatewaySign);
        chain.doFilter(requestWrapper, response);
    }
}
