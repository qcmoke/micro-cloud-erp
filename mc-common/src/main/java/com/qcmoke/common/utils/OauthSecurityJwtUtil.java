package com.qcmoke.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qcmoke.common.dto.CurrentUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

import javax.servlet.http.HttpServletRequest;


/**
 * @author qcmoke
 */
public class OauthSecurityJwtUtil extends OauthSecurityUtil {
    private static final Log logger = LogFactory.getLog(OauthSecurityJwtUtil.class);

    /**
     * 获取当前用户信息
     *
     * @param request HttpServletRequest
     * @return CurrentUser 当前用户信息
     */
    public static CurrentUser getCurrentUser(HttpServletRequest request) {
        JSONObject jwtJson = getJwtClaimsFromHeader(request);
        return getCurrentUser(jwtJson);
    }

    /**
     * 获取当前用户信息
     *
     * @param request ServerHttpRequest
     * @return CurrentUser 当前用户信息
     */
    public static CurrentUser getCurrentUserForWebFlux(ServerHttpRequest request) {
        String accessToken = getAccessTokenForWebFlux(request);
        JSONObject claims = getJwtClaimsFromHeader(accessToken);
        return getCurrentUser(claims);
    }


    /**
     * 获取当前用户名称
     *
     * @param request HttpServletRequest
     * @return 当前用户名称
     */
    public static String getCurrentUsername(HttpServletRequest request) {
        CurrentUser currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return null;
        }
        return currentUser.getUsername();
    }


    /**
     * 使用公钥校验Bearer Token
     *
     * @param accessToken Bearer Token
     * @param publicKey   公钥
     * @return boolean
     */
    public static boolean checkToken(String accessToken, String publicKey) {
        try {
            JwtHelper.decodeAndVerify(accessToken, new RsaVerifier(publicKey));
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    private static CurrentUser getCurrentUser(JSONObject jsonObject) {
        try {
            if (jsonObject == null) {
                return null;
            }
            return JSON.parseObject(JSONObject.toJSONString(jsonObject, SerializerFeature.WriteMapNullValue), CurrentUser.class);
        } catch (Exception e) {
            logger.error("获取当前用户信息失败", e);
            return null;
        }
    }

    private static JSONObject getJwtClaimsFromHeader(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        try {
            //解析jwt
            Jwt decode = JwtHelper.decode(token);
            //得到 jwt中的用户信息
            String claims = decode.getClaims();
            //将jwt转为Map
            return JSON.parseObject(claims);
        } catch (Exception e) {
            logger.error("解析jwt异常", e);
        }
        return null;
    }


    private static JSONObject getJwtClaimsFromHeader(HttpServletRequest request) {
        String token = getAccessToken(request);
        return getJwtClaimsFromHeader(token);
    }
}
