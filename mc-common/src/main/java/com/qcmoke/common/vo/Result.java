package com.qcmoke.common.vo;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author qcmoke
 */
public class Result<T> implements Serializable {
    private Integer status;
    private String message;
    private T data;

    public static <T> Result<T> ok(String message, T data) {
        return new Result<>(HttpStatus.OK.value(), message, data);
    }


    public static <T> Result<T> ok(T data) {
        return ok(null, data);
    }

    public static <T> Result<T> ok(String message) {
        return ok(message, null);
    }

    public static <T> Result<T> ok() {
        return ok(null, null);
    }


    public static <T> Result<T> error(String message) {
        return new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }

    public static <T> Result<T> error(String message, T data) {
        return new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, data);
    }

    public static <T> Result<T> error() {
        return new Result<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, null);
    }


    public static <T> Result<T> unauthorized(String message) {
        return new Result<>(HttpStatus.UNAUTHORIZED.value(), message, null);
    }

    public static <T> Result<T> forbidden(String message) {
        return new Result<>(HttpStatus.FORBIDDEN.value(), message, null);
    }


    private Result() {
    }


    private Result(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }


    public Integer getStatus() {
        return status;
    }

    public Result<T> setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }
}
