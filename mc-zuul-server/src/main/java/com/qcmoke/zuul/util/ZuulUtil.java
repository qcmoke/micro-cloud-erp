package com.qcmoke.zuul.util;

import com.netflix.client.ClientException;
import com.netflix.hystrix.exception.HystrixTimeoutException;
import com.qcmoke.common.dto.Result;
import org.springframework.http.HttpStatus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * @author qcmoke
 */
public class ZuulUtil {

    /**
     * 查找原始源头异常
     *
     * @param e 最顶层异常
     * @return 原始源头异常
     */
    public static Throwable getOriginException(Throwable e) {
        while (e.getCause() != null) {
            e = e.getCause();
        }
        return e;
    }


    /**
     * 构建异常返回格式
     *
     * @param e 最顶层异常
     * @return 异常返回格式
     */
    public static Result<Object> buildErrorResult(Throwable e) {
        Result<Object> error = null;
        Throwable originException = getOriginException(e);
        if (originException instanceof ConnectException) {
            String errorMsg = "服务连接被拒绝！e=" + originException.getMessage();
            error = Result.error(errorMsg).setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        } else if (originException instanceof SocketTimeoutException) {
            String errorMsg = "服务请求处理超时！e=" + originException.getMessage();
            error = Result.error(errorMsg).setStatus(HttpStatus.REQUEST_TIMEOUT.value());
        } else if (originException instanceof ClientException) {
            String errorMsg = "未找到可用服务！=" + originException.getMessage();
            error = Result.error(errorMsg).setStatus(HttpStatus.REQUEST_TIMEOUT.value());
        } else if (originException instanceof HystrixTimeoutException) {
            String errorMsg = "熔断器Hystrix超时异常！e=" + originException.getMessage();
            error = Result.error(errorMsg).setStatus(HttpStatus.REQUEST_TIMEOUT.value());
        } else {
            String errorMsg = "服务端异常,e=" + originException.getMessage();
            Result.error(errorMsg);
        }
        return error;
    }
}