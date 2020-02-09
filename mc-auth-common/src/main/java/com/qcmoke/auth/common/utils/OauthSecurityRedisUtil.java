package com.qcmoke.auth.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qcmoke.auth.common.entity.AuthUser;
import com.qcmoke.common.dto.CurrentUser;
import com.qcmoke.common.utils.OauthSecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.Collection;
import java.util.LinkedHashMap;


/**
 * @author qcmoke
 */
@Slf4j
public class OauthSecurityRedisUtil extends OauthSecurityUtil {


    /**
     * 获取在线用户信息
     *
     * @return CurrentUser 当前用户信息
     */
    public static CurrentUser getCurrentUser() {
        try {
            LinkedHashMap<String, Object> authenticationDetails = getAuthenticationDetails();
            Object principal = authenticationDetails.get("principal");
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(mapper.writeValueAsString(principal), CurrentUser.class);
        } catch (Exception e) {
            log.error("获取当前用户信息失败", e);
            return null;
        }
    }

    /**
     * 获取当前用户名称
     *
     * @return String 用户名
     */
    public static String getCurrentUsername() {
        Object principal = getOAuth2Authentication().getPrincipal();
        if (principal instanceof AuthUser) {
            return ((AuthUser) principal).getUsername();
        }
        return (String) getOAuth2Authentication().getPrincipal();
    }

    /**
     * 获取当前用户权限集
     *
     * @return Collection<GrantedAuthority>权限集
     */
    public static Collection<GrantedAuthority> getCurrentUserAuthority() {
        return getOAuth2Authentication().getAuthorities();
    }

    /**
     * 获取当前令牌内容
     *
     * @return String 令牌内容
     */
    public static String getCurrentTokenValue() {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) getOAuth2Authentication().getDetails();
        return details.getTokenValue();
    }

    public static OAuth2Authentication getOAuth2Authentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (OAuth2Authentication) authentication;
    }

    @SuppressWarnings("all")
    private static LinkedHashMap<String, Object> getAuthenticationDetails() {
        return (LinkedHashMap<String, Object>) getOAuth2Authentication().getUserAuthentication().getDetails();
    }

}
