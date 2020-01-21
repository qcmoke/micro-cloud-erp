package com.qcmoke.security.constant;

/**
 * @author wenxiaohua
 * @version v1.0
 * @description demo
 * @date 2019年11月10日 21:25
 */
public interface SecurityConstants {
    /**
     * 当请求需要身份认证时，默认跳转的url
     */
    String DEFAULT_UNAUTHENTICATION_URL = "/authentication/request";
    /**
     * 默认的用户名密码登录请求处理url
     */
    String DEFAULT_LOGIN_PROCESSING_URL_FORM = "/authentication/form";
    /**
     * 默认表单登录用户名的参数名称
     */
    String DEFAULT_PARAMETER_USERNAME_LOGIN = "username";

    /**
     * 默认表单登录密码的参数名称
     */
    String DEFAULT_PARAMETER_PASSWORD_LOGIN = "password";

    /**
     * 默认的手机验证码登录请求处理url
     */
    String DEFAULT_LOGIN_PROCESSING_URL_MOBILE = "/authentication/mobile";

    /**
     * 验证图片验证码时，http请求中默认的携带图片验证码信息的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_CODE_IMAGE = "imageCode";
    /**
     * 获取验证码controller请求前缀
     */
    String DEFAULT_VALIDATE_CODE_URL_PREFIX = "/code";
    /**
     * 验证短信验证码时，http请求中默认的携带短信验证码信息的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_CODE_SMS = "smsCode";
    /**
     * 发送短信验证码 或 验证短信验证码时，传递手机号的参数的名称
     */
    String DEFAULT_PARAMETER_NAME_MOBILE = "mobile";
    /**
     * session失效默认的跳转地址
     */
    String DEFAULT_SESSION_INVALID_URL = "/session/invalid";

    /**
     * 默认记住我有效时间
     */
    int REMEMBER_ME_SECONDS = 3600;

    /**
     * 如果用户没有配置时的默认登录页
     */
    String LOGIN_PAGE = "/default-login.html";


    /**
     * 如果用户没有配置时的默认注册页
     */
    String SIGN_UP_URL = "/default-signUp.html";

    /**
     * 用户注册接口
     */
    String USER_REGIST_API = "/user/regist";

    /**
     * 生成验证码的字符数
     */
    int SMS_CODE_LENGTH = 4;

    /**
     * 证码的有效期长度(创建验证码后的n秒时间)
     */
    int SMS_CODE_EXPIRE_IN = 60;

    /**
     * 需要图形验证码的地址
     */
    String IMAGE_CODE_URL = "/code/image";
    /**
     * 需要验证码的地址
     */
    String SMS_CODE_URL = "/code/sms";

    /**
     * 验证码图片的宽度
     */
    int IMAGE_CODE_WIDTH = 67;
    /**
     * 验证码图片的高度
     */
    int IMAGE_CODE_Height = 23;
    /**
     * 生成验证码的字符数
     */
    int IMAGE_CODE_Length = 4;
}