package com.qcmoke.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class OauthSecurityJwtUtil extends OauthSecurityUtil {
    private static final Log logger = LogFactory.getLog(OauthSecurityJwtUtil.class);

    public final static String JWT_USER_PROPERTY = "user";
    public final static String JWT_USER_AUTHORITIES_PROPERTY = "authorities";
    public final static String JWT_USER_AUTHORITY_PROPERTY = "authority";

    /**
     * 获取在线用户信息
     *
     * @return CurrentUser 当前用户信息
     */
    public static CurrentUser getCurrentUser(HttpServletRequest request) {
        JSONObject jsonObject = getJwtClaimsFromHeader(request);
        return getCurrentUser(jsonObject);
    }

    public static CurrentUser getCurrentUser(JSONObject jsonObject) {
        try {
            if (jsonObject == null) {
                return null;
            }
            CurrentUser currentUser = JSON.parseObject(
                    JSONObject.toJSONString(jsonObject.get(JWT_USER_PROPERTY), SerializerFeature.WriteMapNullValue),
                    CurrentUser.class);
            JSONArray jsonArray = (JSONArray) jsonObject.get(JWT_USER_AUTHORITIES_PROPERTY);
            Set<String> authorities = JSON.parseObject(JSON.toJSONString(jsonArray, SerializerFeature.WriteMapNullValue), new TypeReference<Set<String>>() {
            });
            currentUser.setAuthorities(authorities);
            return currentUser;
        } catch (Exception e) {
            logger.error("获取当前用户信息失败", e);
            return null;
        }
    }


    /**
     * 获取在线用户名称
     */
    public static String getCurrentUsername(HttpServletRequest request) {
        CurrentUser currentUser = getCurrentUser(request);
        if (currentUser == null) {
            return null;
        }
        return currentUser.getUsername();
    }


    public static JSONObject getJwtClaimsFromHeader(String token) {
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


    public static JSONObject getJwtClaimsFromHeader(HttpServletRequest request) {
        String token = getAccessToken(request);
        return getJwtClaimsFromHeader(token);
    }


    public static JSONObject getPrincipal(JSONObject jwtJsonObject) {
        if (jwtJsonObject == null) {
            return null;
        }
        JSONObject user = (JSONObject) jwtJsonObject.get(JWT_USER_PROPERTY);
        JSONArray parseArray = JSONArray.parseArray(JSON.toJSONString(jwtJsonObject.get(JWT_USER_AUTHORITIES_PROPERTY)));
        user.put(JWT_USER_AUTHORITIES_PROPERTY, new ArrayList<Map<String, String>>() {{
            for (Object authority : parseArray) {
                add(new HashMap<String, String>() {{
                    put(JWT_USER_AUTHORITY_PROPERTY, (String) authority);
                }});
            }
        }});
        return user;
    }

    public static JSONObject getPrincipal(HttpServletRequest request) {
        JSONObject jwtJsonObject = getJwtClaimsFromHeader(request);
        if (jwtJsonObject == null) {
            return null;
        }
        return getPrincipal(jwtJsonObject);
    }

    /**
     * 获取当前用户权限集
     */
    public static JSONArray getCurrentUserAuthority(HttpServletRequest request) {
        JSONObject principal = getPrincipal(request);
        if (principal == null) {
            return null;
        }
        return (JSONArray) principal.get(JWT_USER_AUTHORITIES_PROPERTY);
    }


    public static boolean checkToken(String accessToken, String publicKey) {
        try {
            JwtHelper.decodeAndVerify(accessToken, new RsaVerifier(publicKey));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static CurrentUser getCurrentUserForWebFlux(ServerHttpRequest request) {
        String accessToken = getAccessTokenForWebFlux(request);
        JSONObject claims = getJwtClaimsFromHeader(accessToken);
        return getCurrentUser(claims);
    }
}
