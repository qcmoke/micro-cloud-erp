package com.qcmoke.security.service.impl;

import com.qcmoke.security.service.SmsCodeSenderService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wenxiaohua
 * @version v1.0
 * @description demo
 * @date 2019年11月13日 01:14
 */
@Slf4j
public class DefaultSenderServiceImpl implements SmsCodeSenderService {
    @Override
    public void send(String mobile, String code) {
        //发送验证码给短信服务商,短信服务商在将验证码给客户
        log.info("给" + mobile + "发送短信验证码:" + code);
        System.out.println("给" + mobile + "发送短信验证码:" + code);
    }
}