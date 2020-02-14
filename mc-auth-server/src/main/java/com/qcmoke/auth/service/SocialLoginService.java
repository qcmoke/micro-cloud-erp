package com.qcmoke.auth.service;

import com.alibaba.fastjson.JSONObject;
import com.qcmoke.auth.entity.UserConnection;
import com.qcmoke.auth.exception.SocialException;
import com.qcmoke.auth.vo.BindUserVo;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import java.util.List;

/**
 * @author qcmoke
 */
public interface SocialLoginService {
    AuthRequest getAuthRequest(String source) throws SocialException;

    List<UserConnection> findUserConnections(String username);

    JSONObject resolveLogin(String source, AuthCallback callback) throws SocialException;

    JSONObject resolveBind(String source, AuthCallback callback) throws SocialException;

    OAuth2AccessToken bindLogin(BindUserVo bindUserVo, AuthUser authUser) throws SocialException;

    OAuth2AccessToken signLogin(BindUserVo registerUser, AuthUser authUser) throws SocialException;

    void bind(BindUserVo bindUserVo, AuthUser authUser) throws SocialException;

    void unbind(BindUserVo bindUserVo, String oauthType) throws SocialException;
}
