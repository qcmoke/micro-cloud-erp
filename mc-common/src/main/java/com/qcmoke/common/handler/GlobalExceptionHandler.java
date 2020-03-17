package com.qcmoke.common.handler;

import com.qcmoke.common.exception.GlobalCommonException;
import com.qcmoke.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author qcmoke
 */
@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler extends BaseExceptionHandler {
    @ExceptionHandler(value = GlobalCommonException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleGlobalCommonException(GlobalCommonException e) {
        log.error(e.getMessage());
        return Result.error(e.getMessage());
    }
}
