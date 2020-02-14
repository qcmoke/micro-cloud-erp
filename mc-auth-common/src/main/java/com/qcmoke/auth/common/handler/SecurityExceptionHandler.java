package com.qcmoke.auth.common.handler;

import com.qcmoke.auth.common.exception.NotAllowedAnonymousUserException;
import com.qcmoke.common.dto.Result;
import com.qcmoke.common.handler.GlobalExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author qcmoke
 */
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class SecurityExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(value = AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<Object> handleAccessDeniedException() {
        return Result.forbidden("没有权限访问该资源");
    }

    @ExceptionHandler(value = NotAllowedAnonymousUserException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<Object> handleNotAllowedAnonymousUserException(NotAllowedAnonymousUserException e) {
        return Result.unauthorized(e.getMessage());
    }
}
