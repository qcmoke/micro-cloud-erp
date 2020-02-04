package com.qcmoke.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qcmoke.common.entity.CurrentUser;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OAuthSecurityJwtUtil extends OAuthSecurityUtil {
    private static final Log logger = LogFactory.getLog(OAuthSecurityJwtUtil.class);

    /**
     * 获取在线用户信息
     *
     * @return CurrentUser 当前用户信息
     */
    public static CurrentUser getCurrentUser(HttpServletRequest request) {
        try {
            JSONObject jsonObject = getJwtClaimsFromHeader(request);
            if (jsonObject == null) {
                return null;
            }
            Object principal = jsonObject.get("principal");
            return jsonObject.toJavaObject(CurrentUser.class);
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

    public static JSONObject getJwtClaimsFromHeader(HttpServletRequest request) {
        String token = getAccessToken(request);
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

    public static JSONObject getPrincipal(HttpServletRequest request) {
        JSONObject jwtJsonObject = getJwtClaimsFromHeader(request);
        if (jwtJsonObject == null) {
            return null;
        }
        JSONObject user = (JSONObject) jwtJsonObject.get("user");
        JSONArray parseArray = JSONArray.parseArray(JSON.toJSONString(jwtJsonObject.get("authorities")));
        user.put("authorities", new ArrayList<Map<String, String>>() {{
            for (Object authority : parseArray) {
                add(new HashMap<String, String>() {{
                    put("authority", (String) authority);
                }});
            }
        }});
        return user;
    }

    /**
     * 获取当前用户权限集
     */
    public static JSONArray getCurrentUserAuthority(HttpServletRequest request) {
        JSONObject principal = getPrincipal(request);
        if (principal == null) {
            return null;
        }
        return (JSONArray) principal.get("authorities");

    }

    public static boolean checkToken(String accessToken, String publicKey) {
        try {
            JwtHelper.decodeAndVerify(accessToken, new RsaVerifier(publicKey));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
