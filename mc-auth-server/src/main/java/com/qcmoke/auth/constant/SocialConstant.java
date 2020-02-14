package com.qcmoke.auth.constant;

/**
 * @author qcmoke
 */
public interface SocialConstant {
    String SOCIAL_LOGIN = "social_login";
    String LOGIN_TYPE_BIND = "bind";
    String LOGIN_TYPE_LOGIN = "login";
    /**
     * 第三方social用户密码
     * 在整个认证流程中都只是在本系统中出现，不担心泄露问题。使用这个密码的作用只是为了辅助spring oauth2生成token
     */
    String SOCIAL_USER_PASSWORD = "social123456";
}
