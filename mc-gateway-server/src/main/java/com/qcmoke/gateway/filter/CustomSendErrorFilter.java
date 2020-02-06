package com.qcmoke.gateway.filter;

import com.netflix.client.ClientException;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.qcmoke.common.utils.ResponseWriterUtil;
import com.qcmoke.common.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * 服务 eureka-client 的异常退回处理类
 */
@Component
public class CustomSendErrorFilter extends SendErrorFilter {

    private static final Logger logger = LoggerFactory.getLogger(CustomSendErrorFilter.class);

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        try {
            RequestContext context = RequestContext.getCurrentContext();
            Throwable originException = this.getOriginException(context.getThrowable());
            ZuulException zuulException = getZuulException(originException);
            String errorMsg = zuulException.getMessage() + "；异常具体原因：" + zuulException.errorCause;
            logger.error("e={}", errorMsg);
            ResponseWriterUtil.writeJson(context.getResponse(), zuulException.nStatusCode, Result.error(errorMsg));
        } catch (Exception var5) {
            ReflectionUtils.rethrowRuntimeException(var5);
        }
        return null;
    }


    private Throwable getOriginException(Throwable e) {
        e = e.getCause();
        while (e.getCause() != null) {
            e = e.getCause();
        }
        return e;
    }

    private ZuulException getZuulException(Throwable e) {
        if (e instanceof ConnectException) {
            return new ZuulException("服务连接被拒绝", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        } else if (e instanceof SocketTimeoutException) {
            return new ZuulException("服务请求超时", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        } else if (e instanceof ClientException) {
            return new ZuulException("未找到可用的服务", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        } else {
            return new ZuulException("服务端异常", HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
        }
    }
}
