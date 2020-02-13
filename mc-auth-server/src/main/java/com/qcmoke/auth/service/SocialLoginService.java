package com.qcmoke.auth.service;

import com.alibaba.fastjson.JSONObject;
import com.qcmoke.auth.entity.UserConnection;
import com.qcmoke.auth.exception.SocialException;
import com.qcmoke.auth.vo.BindUser;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.List;

/**
 * @author qcmoke
 */
public interface SocialLoginService {
    AuthRequest getAuthRequest(String source);

    List<UserConnection> findUserConnections(String username);

    JSONObject resolveLogin(String source, AuthCallback callback) throws SocialException;

    JSONObject resolveBind(String source, AuthCallback callback) throws SocialException;

    OAuth2AccessToken bindLogin(BindUser bindUser, AuthUser authUser) throws SocialException;

    OAuth2AccessToken signLogin(BindUser registerUser, AuthUser authUser) throws SocialException;

    void bind(BindUser bindUser, AuthUser authUser) throws SocialException;

    void unbind(BindUser bindUser, String oauthType) throws SocialException;
}
