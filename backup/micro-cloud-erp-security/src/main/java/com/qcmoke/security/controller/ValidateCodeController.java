package com.qcmoke.security.controller;

import com.qcmoke.security.constant.SecurityConstants;
import com.qcmoke.security.validate.ValidateCodeProcessorHolder;
import com.qcmoke.security.validate.code.ValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 获取验证码
 *
 * @author wenxiaohua
 * @version v1.0
 * @description demo
 * @date 2019年11月10日 21:31
 */
@RestController
public class ValidateCodeController {

    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;

    /**
     * 创建验证码，根据验证码类型不同，调用不同SmsCodeGenerator的接口实现
     *
     * @param request
     * @param response
     * @param type     验证正码类型
     * @throws Exception
     */
    @GetMapping(SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/{type}")
    public void createCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {

        ValidateCodeProcessor validateCodeProcessor = validateCodeProcessorHolder.findValidateCodeProcessor(type);

        validateCodeProcessor.create(new ServletWebRequest(request, response));
    }
}