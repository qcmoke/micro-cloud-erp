package com.qcmoke.auth.constant;

/**
 * @author qcmoke
 */
public interface AuthUrlConstant {
    String ALL_RESOURCE_URL = "/resource/**";

    String OAUTH_ALL_URL = "/oauth/**";

    String OAUTH_AUTHORIZE_URL = "/oauth/authorize";

    String OAUTH_CHECK_TOKEN_URL = "/oauth/check_token";

    String OAUTH_CONFIRM_ACCESS_URL = "/oauth/confirm_access";

    String OAUTH_TOKEN_URL = "/oauth/token";

    String OAUTH_TOKEN_KEY_URL = "/oauth/token_key";

    String OAUTH_ERROR_URL = "/oauth/error";

    /**
     * 授权码认证模式登录地址
     */
    String LOGIN_PAGE = "/login";
}
