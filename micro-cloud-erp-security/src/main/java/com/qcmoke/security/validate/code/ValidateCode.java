package com.qcmoke.security.validate.code;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * 验证码信息封装类
 */
public class ValidateCode implements Serializable {

    private static final long serialVersionUID = 1588203828504660915L;

    private String code;

    private LocalDateTime expireTime;

    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    public ValidateCode() {
    }

    /**
     * 是否过期
     *
     * @return boolean
     */
    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }




    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }
}
