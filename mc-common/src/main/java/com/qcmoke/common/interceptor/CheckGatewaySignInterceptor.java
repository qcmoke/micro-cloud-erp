package com.qcmoke.common.interceptor;

import com.qcmoke.common.utils.OauthSecurityUtil;
import com.qcmoke.common.utils.ResponseWriterUtil;
import com.qcmoke.common.utils.security.RSAUtils;
import com.qcmoke.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.SignatureException;

/**
 * 验签网关请求拦截器
 * <p>
 * 校验请求是已经通过网关请求
 *
 * @author qcmoke
 */
@Slf4j
public class CheckGatewaySignInterceptor implements HandlerInterceptor {
    private static final String GATEWAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCx55x0CG1tpdFewgvuOf4c3DLFCGlCpbyASIJhX5pL1o/cGuRcf3/nT5sEpcGcPN2cUnd57BBw8h5/H4422riF6hE71zerGKnsR2lWAmgZmTcghRGLgs+1xKp6sUFQttJSQcIS928iN6VJmlCkF70HT2Ya/n/rfF8ymx8Kx7UdlQIDAQAB";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = OauthSecurityUtil.getBearerToken(request);
        String basicToken = OauthSecurityUtil.getBasicToken(request);

        String content = null;
        String gatewaySign = request.getHeader("gatewaySign");

        if (StringUtils.isBlank(accessToken) && StringUtils.isBlank(basicToken)) {
            ResponseWriterUtil.writeJson(Result.forbidden("非法访问！未携带有效的令牌"));
            return false;
        }
        if (StringUtils.isNoneBlank(accessToken)) {
            content = accessToken;
        } else {
            content = basicToken;
        }

        if (StringUtils.isBlank(gatewaySign) || StringUtils.isBlank(GATEWAY_PUBLIC_KEY)) {
            log.error("网关签名空,gatewaySign={}", gatewaySign);
            ResponseWriterUtil.writeJson(Result.error("网关签名为空,非法访问！请通过网关访问！"));
            return false;
        }
        try {
            //验签
            RSAUtils.doCheck(content, gatewaySign, GATEWAY_PUBLIC_KEY, StandardCharsets.UTF_8.name());
        } catch (SignatureException e) {
            log.error("网关验签令牌失败,非法访问！ e={}", e.getMessage());
            ResponseWriterUtil.writeJson(Result.error("令牌未经网关签名,非法访问！请通过网关访问！"));
            return false;
        }
        return true;
    }
}
