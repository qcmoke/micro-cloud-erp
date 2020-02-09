package com.qcmoke.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @author qcmoke
 */
public class OauthSecurityUtil {

    public static final String HEADER_TOKEN_NAME = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";
    public static final String TOKEN_BASIC_PREFIX = "Basic ";

    private static final Log logger = LogFactory.getLog(OauthSecurityUtil.class);


    public static String getAccessTokenForWebFlux(ServerHttpRequest request) {
        if (request == null) {
            return null;
        }
        //取出头信息
        String authorization = request.getHeaders().getFirst(HEADER_TOKEN_NAME);
        if (StringUtils.isEmpty(authorization) || !authorization.contains(TOKEN_BEARER_PREFIX)) {
            return null;
        }
        //从Bearer 后边开始取出token
        return authorization.substring(7);
    }


    public static String getAccessToken(HttpServletRequest request) {
        if (request == null) {
            return null;
        }
        //取出头信息
        String authorization = request.getHeader(HEADER_TOKEN_NAME);
        if (StringUtils.isEmpty(authorization) || !authorization.contains(TOKEN_BEARER_PREFIX)) {
            return null;
        }
        //从Bearer 后边开始取出token
        return authorization.substring(7);
    }

    public static String getAuthBasicToken(String clientId, String clientSecret) {
        if (clientId == null || clientSecret == null) {
            logger.warn("Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
            return null;
        }
        String token = String.format("%s:%s", clientId, clientSecret);
        return TOKEN_BASIC_PREFIX + new String(Base64.encode(token.getBytes(StandardCharsets.UTF_8)));
    }


    public static String getPublicKeyFromAuthServer() {
        try {
            String oauthServiceUrl = "http://127.0.0.1:9090/oauth/token_key";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set(HEADER_TOKEN_NAME, getAuthBasicToken("admin", "123456"));
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
            ResponseEntity<JSONObject> response = new RestTemplate().exchange(
                    oauthServiceUrl,
                    HttpMethod.GET,
                    entity,
                    JSONObject.class);

            JSONObject body = response.getBody();
            if (body == null) {
                return null;
            }
            return (String) body.get("value");
        } catch (Exception e) {
            String errorMsg = "从认证服务器获取公钥异常,e=" + e.getMessage();
            logger.debug(errorMsg);
            return null;
        }
    }
}