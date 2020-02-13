package com.qcmoke.auth.handler;

import com.qcmoke.auth.common.handler.SecurityExceptionHandler;
import com.qcmoke.auth.exception.SocialException;
import com.qcmoke.common.dto.Result;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author qcmoke
 */
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class AuthExceptionHandler extends SecurityExceptionHandler {

    @ExceptionHandler(value = SocialException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Object> handleSocialException(SocialException e) {
        return Result.error(e.getMessage());
    }
}