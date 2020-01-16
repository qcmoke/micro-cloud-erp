package com.qcmoke.core.utils;

import org.springframework.http.HttpStatus;

public class RespBean {
    private Integer status;
    private String msg;
    private Object obj;

    public static RespBean ok(String msg, Object obj) {
        return new RespBean(HttpStatus.OK.value(), msg, obj);
    }


    public static RespBean ok(Object obj) {
        return ok(null, obj);
    }

    public static RespBean ok(String msg) {
        return ok(msg, null);
    }

    public static RespBean ok() {
        return ok(null, null);
    }


    public static RespBean error(String msg) {
        return new RespBean(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg, null);
    }

    public static RespBean error() {
        return new RespBean(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, null);
    }


    public static RespBean unauthorized(String msg) {
        return new RespBean(HttpStatus.UNAUTHORIZED.value(), msg, null);
    }

    public static RespBean forbidden(String msg) {
        return new RespBean(HttpStatus.FORBIDDEN.value(), msg, null);
    }


    private RespBean() {
    }

    private RespBean(Integer status, String msg, Object obj) {
        this.status = status;
        this.msg = msg;
        this.obj = obj;
    }


    public Integer getStatus() {
        return status;
    }

    public RespBean setStatus(Integer status) {
        this.status = status;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public RespBean setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Object getObj() {
        return obj;
    }

    public RespBean setObj(Object obj) {
        this.obj = obj;
        return this;
    }
}
