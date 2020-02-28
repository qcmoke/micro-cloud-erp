package com.qcmoke.common.interceptor;

import com.qcmoke.common.utils.ResponseWriterUtil;
import com.qcmoke.common.utils.security.RSAUtil;
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
        String gatewaySignContent = request.getHeader("gatewaySignContent");
        String gatewaySignResult = request.getHeader("gatewaySignResult");
        if (StringUtils.isBlank(gatewaySignContent) || StringUtils.isBlank(gatewaySignResult)) {
            log.error("gatewaySignContent={},gatewaySignResult={}", gatewaySignContent, gatewaySignResult);
            ResponseWriterUtil.writeJson(Result.error("网关验签内容为空,非法访问！请通过网关访问！"));
            return false;
        }
        try {
            //验签
            boolean isCheck = RSAUtil.doCheck(gatewaySignContent, gatewaySignResult, GATEWAY_PUBLIC_KEY, StandardCharsets.UTF_8.name());
            if (isCheck) {
                return true;
            }
        } catch (SignatureException e) {
            log.error("网关验签失败,非法访问！ e={}", e.getMessage());
        }
        ResponseWriterUtil.writeJson(Result.error("网关验签失败,非法访问！请通过网关访问！"));
        return false;
    }
}
