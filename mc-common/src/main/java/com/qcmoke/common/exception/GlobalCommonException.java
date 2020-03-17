package com.qcmoke.common.exception;

/**
 * @author qcmoke
 */
public class GlobalCommonException extends RuntimeException {
    public GlobalCommonException(String msg) {
        super(msg);
    }

    public GlobalCommonException(String msg, Throwable t) {
        super(msg, t);
    }
}
