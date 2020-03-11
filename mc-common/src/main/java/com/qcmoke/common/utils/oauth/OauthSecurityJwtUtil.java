package com.qcmoke.common.utils.oauth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qcmoke.common.vo.CurrentUser;
import com.qcmoke.common.utils.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;

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
        JSONObject jwtJson = getJwtJson(request);
        return getCurrentUser(jwtJson);
    }

    /**
     * 获取当前用户信息
     *
     * @return CurrentUser 当前用户信息
     */
    public static CurrentUser getCurrentUser() {
        JSONObject jwtJson = getJwtJson(SpringContextUtil.getHttpServletRequest());
        return getCurrentUser(jwtJson);
    }

    /**
     * 获取当前用户信息
     *
     * @param request ServerHttpRequest
     * @return CurrentUser 当前用户信息
     */
    public static CurrentUser getCurrentUserForWebFlux(ServerHttpRequest request) {
        String accessToken = getBearerTokenForWebFlux(request);
        JSONObject jwtJson = getJwtJson(accessToken);
        return getCurrentUser(jwtJson);
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
     * @param request
     * @return
     */
    public static Long getCurrentUserId(HttpServletRequest request) {
        CurrentUser currentUser = getCurrentUser(getJwtJson(request));
        if (currentUser == null || currentUser.getUserId() == null) {
            return null;
        }
        return currentUser.getUserId();
    }

    private static CurrentUser getCurrentUser(JSONObject jwtJson) {
        try {
            if (jwtJson == null) {
                return null;
            }
            return JSON.parseObject(JSONObject.toJSONString(jwtJson, SerializerFeature.WriteMapNullValue), CurrentUser.class);
        } catch (Exception e) {
            logger.error("获取当前用户信息失败", e);
            return null;
        }
    }

    private static JSONObject getJwtJson(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            return null;
        }
        try {
            //解析jwt
            Jwt decode = JwtHelper.decode(accessToken);
            //得到 jwt中的用户信息
            String claims = decode.getClaims();
            //将jwt转为Map
            return JSON.parseObject(claims);
        } catch (Exception e) {
            logger.error("解析jwt异常", e);
        }
        return null;
    }


    private static JSONObject getJwtJson(HttpServletRequest request) {
        String accessToken = getBearerToken(request);
        return getJwtJson(accessToken);
    }
}
