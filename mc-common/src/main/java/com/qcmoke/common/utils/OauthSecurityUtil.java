package com.qcmoke.common.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @author qcmoke
 */
public class OauthSecurityUtil {
    private static final Log logger = LogFactory.getLog(OauthSecurityUtil.class);

    public static final String HEADER_TOKEN_NAME = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";
    public static final String TOKEN_BASIC_PREFIX = "Basic ";


    /**
     * 从请求头里获取Bearer Token
     *
     * @param request HttpServletRequest
     * @return Bearer Token
     */
    public static String getBearerToken(HttpServletRequest request) {
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


    /**
     * 从请求头里获取Bearer Token
     *
     * @param request ServerHttpRequest
     * @return Bearer Token
     */
    public static String getBearerTokenForWebFlux(ServerHttpRequest request) {
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

    /**
     * 从请求头里 Basic Token
     *
     * @param request HttpServletRequest
     * @return Basic Token
     */
    public static String getBasicToken(HttpServletRequest request) {
        String authorization = request.getHeader(HEADER_TOKEN_NAME);
        if (StringUtils.isEmpty(authorization) || !authorization.contains(TOKEN_BASIC_PREFIX)) {
            return null;
        }
        return authorization.substring(6);
    }


    /**
     * 从请求头里 Basic Token
     *
     * @param request ServerHttpRequest
     * @return Basic Token
     */
    public static String getBasicTokenForWebFlux(ServerHttpRequest request) {
        String authorization = request.getHeaders().getFirst(HEADER_TOKEN_NAME);
        if (StringUtils.isEmpty(authorization) || !authorization.contains(TOKEN_BASIC_PREFIX)) {
            return null;
        }
        return authorization.substring(6);
    }


    /**
     * 生成basic token
     *
     * @param clientId     clientId
     * @param clientSecret clientSecret
     * @return basic token
     */
    public static String generateBasicToken(String clientId, String clientSecret) {
        if (clientId == null || clientSecret == null) {
            logger.warn("Null Client ID or Client Secret detected. Endpoint that requires authentication will reject request with 401 error.");
            return null;
        }
        String token = String.format("%s:%s", clientId, clientSecret);
        return TOKEN_BASIC_PREFIX + new String(Base64.encodeBase64(token.getBytes(StandardCharsets.UTF_8)));
    }


    /**
     * 从认证服务器获取公钥
     *
     * @param oauthServiceIp 服务端请求ip地址
     * @param clientId       oauth clientId
     * @param clientSecret   oauth clientSecret
     * @return 公钥
     */
    public static String getPublicKeyFromAuthServer(String oauthServiceIp, String oauthServicePort, String clientId, String clientSecret) {
        try {
            //String oauthServiceUrl = "http://127.0.0.1:9090/oauth/token_key";
            String oauthServiceUrl = "http://" + oauthServiceIp + ":" + oauthServicePort + "/oauth/token_key";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.set(HEADER_TOKEN_NAME, generateBasicToken(clientId, clientSecret));
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
