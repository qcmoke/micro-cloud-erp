package com.qcmoke.security.validate.code.sms;

import com.qcmoke.security.validate.code.ValidateCode;
import lombok.Data;

import java.io.Serializable;

/**
 * 短信验证码
 */
@Data
public class SmsCode extends ValidateCode implements Serializable {
    public SmsCode(String code, int expireIn) {
        super(code, expireIn);
    }
}