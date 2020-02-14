package com.qcmoke.gateway.handler;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qcmoke.common.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.cloud.gateway.support.TimeoutException;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;


/**
 * @author qcmoke
 */
@Slf4j
public class GatewayExceptionHandler extends DefaultErrorWebExceptionHandler {

    public GatewayExceptionHandler(ErrorAttributes errorAttributes, ResourceProperties resourceProperties,
                                   ErrorProperties errorProperties, ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    /**
     * 异常处理，定义返回报文格式
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Result<Object> result = null;
        Throwable error = super.getError(request);
        log.error("请求发生异常，请求URI：{}，请求方法：{}，异常信息：{}", request.path(), request.methodName(), error.getMessage());
        if (error instanceof NotFoundException) {
            String serverId = StringUtils.substringAfterLast(error.getMessage(), "Unable to find instance for ");
            serverId = StringUtils.replace(serverId, "\"", StringUtils.EMPTY);
            String errorMessage = String.format("无法找到%s服务", serverId);
            result = Result.error(errorMessage).setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        } else if (StringUtils.containsIgnoreCase(error.getMessage(), "connection refused")) {
            result = Result.error().setStatus(HttpStatus.SERVICE_UNAVAILABLE.value()).setMessage("目标服务拒绝连接");
        } else if (error instanceof TimeoutException) {
            result = Result.error("访问服务超时").setStatus(HttpStatus.REQUEST_TIMEOUT.value());
        } else if (error instanceof ResponseStatusException && StringUtils.containsIgnoreCase(error.getMessage(), HttpStatus.NOT_FOUND.toString())) {
            result = Result.error("未找到该资源").setStatus(HttpStatus.NOT_FOUND.value());
        } else {
            result = Result.error("网关转发异常").setStatus(HttpStatus.BAD_GATEWAY.value());
        }
        return JSON.parseObject(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
    }

    @Override
    @SuppressWarnings("all")
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * 根据status获取对应的HttpStatus
     */
    @Override
    protected HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {
        int status = (int) errorAttributes.get("status");
        return HttpStatus.valueOf(status);
    }
}
