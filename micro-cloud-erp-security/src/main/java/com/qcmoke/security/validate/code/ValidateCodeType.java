package com.qcmoke.security.validate.code;

import com.qcmoke.security.constant.SecurityConstants;

/**
 * 验证码的类型
 *
 * @author wenxiaohua
 * @version v1.0
 * @description demo
 * @date 2019年11月13日 02:25
 */
public enum ValidateCodeType {

    /**
     * 短信验证码
     */
    SMS {
        @Override
        public String getParamNameOnValidate() {
            return SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_SMS;
        }
    },
    /**
     * 图片验证码
     */
    IMAGE {
        @Override
        public String getParamNameOnValidate() {
            return SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_IMAGE;
        }
    };

    /**
     * 校验时从请求中获取的参数的名字
     */
    public abstract String getParamNameOnValidate();

}
