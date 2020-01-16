package com.qcmoke.security.validate;

import com.qcmoke.security.exception.ValidateCodeException;
import com.qcmoke.security.validate.code.ValidateCodeProcessor;
import com.qcmoke.security.validate.code.ValidateCodeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 校验码处理器映射器
 * 通过验证码的类型用对应的ValidateCodeProcessor
 *
 * @author wenxiaohua
 * @version v1.0
 * @description demo
 * @date 2019年11月13日 02:24
 */
@Component
public class ValidateCodeProcessorHolder {

    /**
     * spring会将ioc容器中所有的ValidateCodeProcessor实现类对象注入到map中，key是bean组件的name，value是bean组价对象
     */
    @Autowired
    private Map<String, ValidateCodeProcessor> urlAndProcessorMap;

    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
        return findValidateCodeProcessor(type.toString().toLowerCase());
    }

    /**
     * @param type 验证码类型名称
     * @return [type]+ValidateCodeProcessor = typeValidateCodeProcessor （如/code/image => imageValidateCodeProcessor）
     */
    public ValidateCodeProcessor findValidateCodeProcessor(String type) {
        String name = type.toLowerCase() + ValidateCodeProcessor.class.getSimpleName();
        ValidateCodeProcessor processor = urlAndProcessorMap.get(name);
        if (processor == null) {
            throw new ValidateCodeException("验证码处理器" + name + "不存在");
        }
        return processor;
    }
}