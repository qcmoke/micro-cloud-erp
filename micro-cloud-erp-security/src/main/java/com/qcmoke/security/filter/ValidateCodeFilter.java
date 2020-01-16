package com.qcmoke.security.filter;

import com.qcmoke.security.constant.SecurityConstants;
import com.qcmoke.security.exception.ValidateCodeException;
import com.qcmoke.security.validate.ValidateCodeProcessorHolder;
import com.qcmoke.security.validate.code.ValidateCodeProcessor;
import com.qcmoke.security.validate.code.ValidateCodeType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 多验证码支持校验过滤器
 * 根据/code/type 的type参数选择验证码类型生成器
 * 实现InitializingBean接口，让创建对象执行构造函数之后执行afterPropertiesSet方法
 */
@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {
    /**
     * 验证失败处理器
     */
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 校验码处理器映射器
     */
    @Autowired
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;
    /**
     * 存放所有需要校验验证码的url和验证码类型映射
     */
    private Map<String, ValidateCodeType> urlCodeTypeMap = new HashMap<>();
    /**
     * url匹配器（验证请求url与配置的url是否匹配的工具类）
     */
    private AntPathMatcher pathMatcher = new AntPathMatcher();


    /**
     * 初始化需要校验验证码的url
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        /**
         * 默认配置
         */
        //映射“默认的表单登录验证请求地址”和“图形验证码类型”
        urlCodeTypeMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);
        //映射“默认的手机短信认证地址”和“手机短信验证码类型”
        urlCodeTypeMap.put(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE, ValidateCodeType.SMS);

        /**
         * 用户自定义配置
         */
        addUrlToMap(SecurityConstants.IMAGE_CODE_URL, ValidateCodeType.IMAGE);
        addUrlToMap(SecurityConstants.SMS_CODE_URL, ValidateCodeType.SMS);
    }


    /**
     * 根据请求的地址获取验证码的类型，再用该类型对应的ValidateCodeProcessor进行校验，只对post请求做校验
     * 拦截处理
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        ValidateCodeType type = getValidateCodeTypeOnlyForPostRequest(request.getRequestURI(), request.getMethod());
        if (type != null) {
            logger.info("校验请求(" + request.getRequestURI() + ")中的验证码,验证码类型" + type);
            try {
                //根据type到ioc容器获取ValidateCodeProcessor的对应实现类
                ValidateCodeProcessor validateCodeProcessor = validateCodeProcessorHolder.findValidateCodeProcessor(type);
                //用找到的ValidateCodeProcessor进行验证处理
                validateCodeProcessor.validate(new ServletWebRequest(request, response));
                logger.info("验证码校验通过");
            } catch (ValidateCodeException exception) {
                authenticationFailureHandler.onAuthenticationFailure(request, response, exception);
                return;
            }
        }
        chain.doFilter(request, response);
    }


    /**
     * 获取校验码的类型，如果当前请求不需要校验，则返回null
     * 只校验post请求
     *
     * @param requestUrl    请求的url
     * @param requestMethod 请求方式
     * @return ValidateCodeType
     */
    private ValidateCodeType getValidateCodeTypeOnlyForPostRequest(String requestUrl, String requestMethod) {
        ValidateCodeType result = null;
        if (!StringUtils.equalsIgnoreCase(requestMethod, "get")) {
            Set<String> urls = urlCodeTypeMap.keySet();
            for (String url : urls) {
                if (pathMatcher.match(url, requestUrl)) {
                    result = urlCodeTypeMap.get(url);
                }
            }
        }
        return result;
    }


    /**
     * 映射需要校验验证码的URL和验证码的类型 （将需要校验验证码的URL根据校验的类型放入map）
     *
     * @param urlString        需要被校验的地址(支持多个地址，多个需要使用英文逗号分隔)
     * @param validateCodeType 验证码枚举类型
     */
    protected void addUrlToMap(String urlString, ValidateCodeType validateCodeType) {
        if (StringUtils.isNotBlank(urlString)) {
            String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(urlString, ",");
            for (String url : urls) {
                urlCodeTypeMap.put(url, validateCodeType);
            }
        }
    }
}