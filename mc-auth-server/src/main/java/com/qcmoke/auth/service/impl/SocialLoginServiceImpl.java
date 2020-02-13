package com.qcmoke.auth.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qcmoke.auth.constant.GrantTypeConstant;
import com.qcmoke.auth.constant.ParamsConstant;
import com.qcmoke.auth.constant.SocialConstant;
import com.qcmoke.auth.entity.User;
import com.qcmoke.auth.entity.UserConnection;
import com.qcmoke.auth.entity.UserRole;
import com.qcmoke.auth.exception.SocialException;
import com.qcmoke.auth.mapper.UserMapper;
import com.qcmoke.auth.mapper.UserRoleMapper;
import com.qcmoke.auth.properties.Oauth2SocialProperties;
import com.qcmoke.auth.service.SocialLoginService;
import com.qcmoke.auth.service.UserConnectionService;
import com.qcmoke.auth.vo.BindUser;
import com.qcmoke.common.utils.SpringContextUtil;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthGithubRequest;
import me.zhyd.oauth.request.AuthQqRequest;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWeChatRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qcmoke
 */
@Service
public class SocialLoginServiceImpl implements SocialLoginService {

    @Autowired
    private UserConnectionService userConnectionService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private JdbcClientDetailsService jdbcClientDetailsService;
    @Autowired
    private ResourceOwnerPasswordTokenGranter granter;
    @Autowired
    private Oauth2SocialProperties oauth2SocialProperties;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UserConnection> findUserConnections(String username) {
        return this.userConnectionService.selectByCondition(username);
    }


    @Override
    public JSONObject resolveLogin(String socialType, AuthCallback callback) throws SocialException {
        AuthRequest authRequest = this.getAuthRequest(socialType);
        AuthResponse<?> response = authRequest.login(callback);
        if (!response.ok()) {
            throw new SocialException(String.format("第三方登录失败，%s", response.getMsg()));
        }
        AuthUser authUser = (AuthUser) response.getData();
        UserConnection userConnection = userConnectionService.selectByCondition(authUser.getSource().toString(), authUser.getUuid());
        if (userConnection == null) {
            //第三方登录未绑定，状态码设置为8888
            JSONObject result = new JSONObject();
            result.put("message", "not_bind");
            result.put("data", authUser);
            return result;
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, userConnection.getUsername()));
        if (user == null) {
            throw new SocialException("系统中未找到与第三方账号对应的账户");
        }
        //到数据库获取ClientDetail并得到OAuth2AccessToken
        OAuth2AccessToken oAuth2AccessToken = getOauth2AccessToken(user);
        //第三方登录成功。设置状态码为9999
        JSONObject result = new JSONObject();
        result.put("message", "social_login_success");
        result.put("data", oAuth2AccessToken);
        result.put("username", user.getUsername());
        return result;
    }


    private OAuth2AccessToken getOauth2AccessToken(User user) throws SocialException {

        //设置参数给UserDetailsService作登录类型判断
        HttpServletRequest httpservletrequest = SpringContextUtil.getHttpServletRequest();
        httpservletrequest.setAttribute(ParamsConstant.LOGIN_TYPE, SocialConstant.SOCIAL_LOGIN);

        //根据client id 查询数据库获取ClientDetails
        ClientDetails clientDetails = jdbcClientDetailsService.loadClientByClientId(oauth2SocialProperties.getClientId());
        if (clientDetails == null) {
            throw new SocialException("未找到第三方登录可用的Client");
        }
        Map<String, String> requestParameters = new HashMap<>(5);
        requestParameters.put("grant_type", GrantTypeConstant.PASSWORD);
        requestParameters.put("username", user.getUsername());
        requestParameters.put("password", oauth2SocialProperties.getSocialUserPassword());
        String grantTypes = String.join(",", clientDetails.getAuthorizedGrantTypes());
        TokenRequest tokenRequest = new TokenRequest(requestParameters, clientDetails.getClientId(), clientDetails.getScope(), grantTypes);
        return granter.grant(GrantTypeConstant.PASSWORD, tokenRequest);
    }

    @Override
    public JSONObject resolveBind(String source, AuthCallback callback) throws SocialException {
        AuthRequest authRequest = this.getAuthRequest(source);
        AuthResponse<?> response = authRequest.login(callback);
        if (!response.ok()) {
            throw new SocialException(String.format("第三方登录失败，%s", response.getMsg()));
        }
        JSONObject result = new JSONObject();
        result.put("data", response.getData());
        return result;
    }

    @Override
    public OAuth2AccessToken bindLogin(BindUser bindUser, AuthUser authUser) throws SocialException {
        User systemUser = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, bindUser.getBindUsername()));
        if (systemUser == null || !passwordEncoder.matches(bindUser.getBindPassword(), systemUser.getPassword())) {
            throw new SocialException("绑定系统账号失败，用户名或密码错误！");
        }
        this.createConnection(systemUser, authUser);
        return getOauth2AccessToken(systemUser);
    }


    @Override
    public OAuth2AccessToken signLogin(BindUser registerUser, AuthUser authUser) throws SocialException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, registerUser.getBindUsername()));
        if (user != null) {
            throw new SocialException("该用户名已存在！");
        }
        String encryptPassword = passwordEncoder.encode(registerUser.getBindPassword());

        User systemUser = new User();
        systemUser.setUsername(registerUser.getBindUsername());
        systemUser.setPassword(encryptPassword);
        systemUser.setCreateTime(new Date());
        systemUser.setStatus(1);
        systemUser.setSex("2");
        systemUser.setAvatar("default.jpg");
        systemUser.setDescription("注册用户");
        userMapper.insert(systemUser);

        UserRole userRole = new UserRole();
        userRole.setUid(systemUser.getUid());
        // 注册用户角色 ID
        userRole.setRid(2L);
        userRoleMapper.insert(userRole);

        this.createConnection(systemUser, authUser);
        return getOauth2AccessToken(systemUser);
    }

    private void createConnection(User systemUser, AuthUser authUser) throws SocialException {
        UserConnection userConnection = new UserConnection();
        userConnection.setUsername(systemUser.getUsername());
        userConnection.setProviderName(authUser.getSource().toString());
        userConnection.setProviderUserId(authUser.getUuid());
        userConnection.setProviderUsername(authUser.getUsername());
        userConnection.setImageUrl(authUser.getAvatar());
        userConnection.setNickName(authUser.getNickname());
        userConnection.setLocation(authUser.getLocation());
        boolean isSaved = userConnectionService.save(userConnection);
        if (!isSaved) {
            throw new SocialException("绑定系统账号失败，新增失败！");
        }
    }


    @Override
    public void bind(BindUser bindUser, AuthUser authUser) throws SocialException {
        String username = bindUser.getBindUsername();
        if (isCurrentUser(username)) {
            UserConnection userConnection = userConnectionService.selectByCondition(authUser.getSource().toString(), authUser.getUuid());
            if (userConnection != null) {
                throw new SocialException("绑定失败，该第三方账号已绑定" + userConnection.getUsername() + "系统账户");
            }
            User systemUser = new User();
            systemUser.setUsername(username);
            this.createConnection(systemUser, authUser);
        } else {
            throw new SocialException("绑定失败，您无权绑定别人的账号");
        }
    }

    @Override
    public void unbind(BindUser bindUser, String oauthType) throws SocialException {
        String username = bindUser.getBindUsername();
        if (isCurrentUser(username)) {
            userConnectionService.remove(new LambdaQueryWrapper<UserConnection>().eq(UserConnection::getUsername, username).eq(UserConnection::getProviderName, oauthType));
        } else {
            throw new SocialException("解绑失败，您无权解绑别人的账号");
        }
    }

    private boolean isCurrentUser(String username) {
        String currentUsername = OauthSecurityJwtUtil.getCurrentUsername(SpringContextUtil.getHttpServletRequest());
        return StringUtils.equalsIgnoreCase(username, currentUsername);
    }


    /**
     * 根据具体的授权来源，获取授权请求工具类
     */
    @Override
    public AuthRequest getAuthRequest(String source) {
        AuthRequest authRequest = null;
        switch (source) {
            case "qq":
                authRequest = new AuthQqRequest(AuthConfig.builder()
                        .clientId("101448999")
                        .clientSecret("1d958787a87559bad371c0a9e26eef61")
                        .redirectUri("http://www.merryyou.cn/login/qq")
                        .build());
                break;
            case "weixin":
                authRequest = new AuthWeChatRequest(AuthConfig.builder()
                        .clientId("wxd99431bbff8305a0")
                        .clientSecret("60f78681d063590a469f1b297feff3c4")
                        .redirectUri("http://www.pinzhi365.com/qqLogin/weixin")
                        .build());
                break;
            case "github":
                authRequest = new AuthGithubRequest(AuthConfig.builder()
                        .clientId("a0d287406edbdb70394d")
                        .clientSecret("0dc78340e6a563d562b29adc1d17d9816a9014f9")
                        .redirectUri("http://127.0.0.1:9070/auth/resource/social/callback/github")
                        .build());
            default:
                break;
        }
        if (null == authRequest) {
            throw new AuthException("未获取到有效的Auth配置");
        }
        return authRequest;
    }
}
