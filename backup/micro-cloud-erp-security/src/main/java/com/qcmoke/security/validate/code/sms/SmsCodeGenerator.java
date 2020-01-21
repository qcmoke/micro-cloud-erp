package com.qcmoke.security.validate.code.sms;

import com.qcmoke.security.constant.SecurityConstants;
import com.qcmoke.security.validate.code.ValidateCodeGenerator;
import org.apache.commons.lang3.RandomStringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 短信验证码生成器
 */
public class SmsCodeGenerator implements ValidateCodeGenerator {
    @Override
    public SmsCode generate(HttpServletRequest request) {
        int length = SecurityConstants.SMS_CODE_LENGTH;
        int expireIn = SecurityConstants.SMS_CODE_EXPIRE_IN;
        String code = RandomStringUtils.randomNumeric(length);
        return new SmsCode(code, expireIn);
    }
}