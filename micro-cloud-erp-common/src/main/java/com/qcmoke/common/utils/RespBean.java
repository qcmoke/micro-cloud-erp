package com.qcmoke.common.utils;

import org.springframework.http.HttpStatus;

public class RespBean {
    private Integer status;
    private String message;
    private Object data;

    public static RespBean ok(String message, Object data) {
        return new RespBean(HttpStatus.OK.value(), message, data);
    }


    public static RespBean ok(Object data) {
        return ok(null, data);
    }

    public static RespBean ok(String message) {
        return ok(message, null);
    }

    public static RespBean ok() {
        return ok(null, null);
    }


    public static RespBean error(String message) {
        return new RespBean(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    public static RespBean error() {
        return new RespBean(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, null);
    }


    public static RespBean unauthorized(String message) {
        return new RespBean(HttpStatus.UNAUTHORIZED.value(), message, null);
    }

    public static RespBean forbidden(String message) {
        return new RespBean(HttpStatus.FORBIDDEN.value(), message, null);
    }


    private RespBean() {
    }

    private RespBean(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }


    public Integer getStatus() {
        return status;
    }

    public RespBean setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RespBean setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public RespBean setData(Object data) {
        this.data = data;
        return this;
    }
}
