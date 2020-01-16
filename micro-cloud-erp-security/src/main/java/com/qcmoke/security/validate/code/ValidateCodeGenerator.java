package com.qcmoke.security.validate.code;

import javax.servlet.http.HttpServletRequest;

/**
 * 校验码生成器
 * @author zhailiang
 *
 */
public interface ValidateCodeGenerator {

    /**
     * 生成校验码
     * @param request
     * @return
     */
    ValidateCode generate(HttpServletRequest request);
}
