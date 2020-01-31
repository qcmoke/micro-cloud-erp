package com.qcmoke.common.exception;

import org.springframework.security.authentication.InsufficientAuthenticationException;

/**
 * 不支持匿名用户授权异常
 */
public class NotAllowedAnonymousUserException extends InsufficientAuthenticationException {
    public NotAllowedAnonymousUserException(String msg) {
        super(msg);
    }
}
