package com.qcmoke.common.interceptor;

import com.qcmoke.common.utils.OAuthSecurityJwtUtil;
import com.qcmoke.common.utils.OAuthSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SecurityTokenInterceptor implements HandlerInterceptor {
    String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwlJSUTtDeUML9LYp6X/r\n" +
            "5/3LxdKaLLwpTPr+OHPYjRnYCdYXpG/CN13A+fubmX0Hs8zLfSN7Y+Ao93g1VDou\n" +
            "Y6FPcrDqoQ71AuH7lZG2eqHyOaOYKgfUSAG8N6I2OWZ7z1t1dRSBJbioP0xAic+O\n" +
            "8O31Mej23l64yuV9GogWRu0XjKam5fLumWLMmGGlMVYYZHUgX1AFbZQzgrophmug\n" +
            "dm5EOQWtok6Y3tS2/vbjVh/x/MNFMWsUVFjMi9t8UUU1v2S7twL/oPDXIc+lqoPX\n" +
            "UqqGXX5EZ0zn+yzwx2Ge53vTTut64wgrZCYuothNU0ULkfSVoW8SjJUjSwPJAKDI\n" +
            "0wIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("token验证。。。");
        if (OAuthSecurityJwtUtil.checkToken(OAuthSecurityUtil.getAccessToken(request), publicKey)) {
            log.info("token验证通过。。。");
            return true;
        }
        log.error("token验证失败。。。");
        return false;
    }
}
