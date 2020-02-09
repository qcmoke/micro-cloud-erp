package com.qcmoke.gateway.filter;

import com.qcmoke.common.utils.OauthSecurityUtil;
import com.qcmoke.common.utils.security.RSAUtils;
import com.qcmoke.gateway.wrapper.HeaderMapRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SignatureException;

/**
 * @author qcmoke
 */
@Component
@Slf4j
public class AuthGatewaySignFiler extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
        String gatewayPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCx55x0CG1tpdFewgvuOf4c3DLFCGlCpbyASIJhX5pL1o/cGuRcf3/nT5sEpcGcPN2cUnd57BBw8h5/H4422riF6hE71zerGKnsR2lWAmgZmTcghRGLgs+1xKp6sUFQttJSQcIS928iN6VJmlCkF70HT2Ya/n/rfF8ymx8Kx7UdlQIDAQAB";
        String gatewayPrivateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALHnnHQIbW2l0V7CC+45/hzcMsUIaUKlvIBIgmFfmkvWj9wa5Fx/f+dPmwSlwZw83ZxSd3nsEHDyHn8fjjbauIXqETvXN6sYqexHaVYCaBmZNyCFEYuCz7XEqnqxQVC20lJBwhL3byI3pUmaUKQXvQdPZhr+f+t8XzKbHwrHtR2VAgMBAAECgYAaykMAIijAY0EFIPmE9Uyz8eDfVOXs+GJLex/PJANrOjNNtOsAlt6e6ZjxeTiPm4bPvIdrX8YWDA/Vmt3imstAdsstcFHGGgRryaSVkLtklHoCOirEMJR84SIwx7coro8uRcJVY+lo6r40uCq/4naAa11Wif49DYsMsNmNiCRNgQJBAOYXqrh1V/E4HgTxgkwJeEBbb8CoCSDGxnzxT0wsflqXMJ1VmSVNJ/eV2F7WIg9cK0umJBaFKEvGXVx9faQg6bUCQQDF76be6mq2fTLGViAzX+tODUuLp+/q9B55eVwcvJ21s3HS5GRI0AMeILtvvC+rFrn8LoW/hOqtBxI0sJYl5FBhAkEA2a2QPHv/G90cQU7+FtNqqXAXtGsEX7bN90wP2h/J1ghs3Jwri2eIJSnlDiuFA4UODL58K7YD3lQm5SZvo8PjdQJAadyZyAFh34YoYNFxWWjEpbMQo3nHJEc6AUf6DtiGFMcLanqCdDrkX/mrpb/lUsDN6eVL3TmOdcohX5LOSyfIIQJBANQwNmTer7NnwgQLD1utxgaNHXcrNEdpj3OLUiXcupbLWS1dgCysKmrSJYUsuSFGjnDV5uMYUCU1+uJSuJSkJXQ=";
        String rsaSignContent = null;
        try {
            rsaSignContent = RSAUtils.rsaSign(OauthSecurityUtil.getAccessToken(request), gatewayPrivateKey, "utf-8");
        } catch (SignatureException e) {
            log.error("签名异常");
        }
        requestWrapper.addHeader("gatewaySign", rsaSignContent);
        requestWrapper.addHeader("gatewayPublicKey", gatewayPublicKey);
        chain.doFilter(requestWrapper, response);
    }
}
