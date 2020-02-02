package com.qcmoke.common.utils;

import org.springframework.http.HttpStatus;

public class Result {
    private Integer status;
    private String message;
    private Object data;

    public static Result ok(String message, Object data) {
        return new Result(HttpStatus.OK.value(), message, data);
    }


    public static Result ok(Object data) {
        return ok(null, data);
    }

    public static Result ok(String message) {
        return ok(message, null);
    }

    public static Result ok() {
        return ok(null, null);
    }


    public static Result error(String message) {
        return new Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    public static Result error() {
        return new Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, null);
    }


    public static Result unauthorized(String message) {
        return new Result(HttpStatus.UNAUTHORIZED.value(), message, null);
    }

    public static Result forbidden(String message) {
        return new Result(HttpStatus.FORBIDDEN.value(), message, null);
    }


    private Result() {
    }

    private Result(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }


    public Integer getStatus() {
        return status;
    }

    public Result setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }
}
