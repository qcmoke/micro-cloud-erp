package com.qcmoke.security.config;

import com.qcmoke.security.service.SmsCodeSenderService;
import com.qcmoke.security.service.impl.DefaultSenderServiceImpl;
import com.qcmoke.security.validate.code.ValidateCodeGenerator;
import com.qcmoke.security.validate.code.image.ImageCodeGenerator;
import com.qcmoke.security.validate.code.sms.SmsCodeGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 条件创建bean配置类
 * 以下这些bean都可以被覆盖
 */
@Configuration
public class SecurityBeanConfig {

    /**
     * @ConditionalOnMissingBean 注解：
     * 仅仅在当前上下文中不存在某个对象时，才会实例化一个Bean。如果用户引用方实现了ValidateCodeGenerator，那么ImageCodeGenerator还会被覆盖
     * 参数可以是组件的name，也可以是组件的class
     */


    /**
     * ValidateCodeProcessorHolder规定实现类要以ValidateCodeGenerator为后缀，前缀是请求参数名称，比如url=/code/image对应imageValidateCodeGenerator
     *
     * @return ValidateCodeGenerator
     */
    @Bean
    @ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
    public ValidateCodeGenerator imageValidateCodeGenerator() {
        return new ImageCodeGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(name = "smsValidateCodeGenerator")
    public ValidateCodeGenerator smsValidateCodeGenerator() {
        return new SmsCodeGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(name = "defaultSenderServiceImpl")
    public SmsCodeSenderService defaultSenderServiceImpl() {
        return new DefaultSenderServiceImpl();
    }
}