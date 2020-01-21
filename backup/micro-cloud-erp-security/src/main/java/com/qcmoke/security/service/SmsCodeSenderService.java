package com.qcmoke.security.service;

/**
 * 短信验证码发送器
 */
public interface SmsCodeSenderService {
    /**
     * 发送短信验证码给客户
     *
     * @param mobile 客户的手机号码
     * @param code   短信验证码
     */
    void send(String mobile, String code);
}
