package com.qcmoke.security.validate.code.sms;


import com.qcmoke.security.constant.SecurityConstants;
import com.qcmoke.security.validate.AbstractValidateCodeProcessor;
import com.qcmoke.security.validate.code.ValidateCode;
import com.qcmoke.security.service.SmsCodeSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 短信验证码处理器
 */
@Component("smsValidateCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    /**
     * 短信验证码发送器
     */
    @Autowired
    private SmsCodeSenderService smsCodeSenderService;

    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
        String paramName = SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE;
        String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), paramName);
        smsCodeSenderService.send(mobile, validateCode.getCode());
    }

}
