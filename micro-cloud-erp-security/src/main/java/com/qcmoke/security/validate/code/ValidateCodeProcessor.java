package com.qcmoke.security.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码处理器，封装不同校验码的处理逻辑
 *
 * @author wenxiaohua
 * @version v1.0
 * @description demo
 * @date 2019年11月13日 02:24
 */
public interface ValidateCodeProcessor {

    /**
     * 验证码放入session时的前缀
     */
    String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

    /**
     * 创建校验码
     */
    void create(ServletWebRequest request) throws Exception;

    /**
     * 校验验证码
     */
    void validate(ServletWebRequest request);

}