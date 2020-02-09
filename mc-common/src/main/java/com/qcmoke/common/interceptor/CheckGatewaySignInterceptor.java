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
import java.security.SignatureException;

/**
 * 验签网关请求拦截器
 * 校验请求是已经通过网关请求
 *
 * @author qcmoke
 */
@Slf4j
public class CheckGatewaySignInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String accessToken = OauthSecurityUtil.getAccessToken(request);
        String gatewaySign = request.getHeader("gatewaySign");
        String gatewayPublicKey = request.getHeader("gatewayPublicKey");
        if (StringUtils.isBlank(accessToken)) {
            doError("非法访问！未携带有效的accessToken！");
            return false;
        }
        if (StringUtils.isBlank(gatewaySign) || StringUtils.isBlank(gatewayPublicKey)) {
            log.error("网关签名或者网关公钥为空,gatewaySign={},gatewayPublicKey={}", gatewaySign, gatewayPublicKey);
            doError("gatewaySign 验签失败,非法访问！请通过网关访问！");
            return false;
        }
        boolean isOk = false;
        try {
            isOk = RSAUtils.doCheck(accessToken, gatewaySign, gatewayPublicKey, "utf-8");
        } catch (SignatureException e) {
            doError("gatewaySign 验签失败,非法访问！e=" + e.getMessage());
            return false;
        }
        if (!isOk) {
            doError("gatewaySign 验签失败,非法访问！请通过网关访问！");
            return false;
        }
        return true;
    }

    private void doError(String errorMsg) {
        log.error(errorMsg);
        ResponseWriterUtil.writeJson(Result.forbidden(errorMsg));
    }
}
