package com.qcmoke.auth.common.exception;

import org.springframework.security.authentication.InsufficientAuthenticationException;

/**
 * 不支持匿名用户授权异常
 * @author qcmoke
 */
public class NotAllowedAnonymousUserException extends InsufficientAuthenticationException {
    public NotAllowedAnonymousUserException(String msg) {
        super(msg);
    }
}
