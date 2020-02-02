package com.qcmoke.common.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcResult<T> implements Serializable {
    //状态码
    private Integer status;
    //提示信息
    private String message;
    //返回数据
    private T data;

    public final static int SUCCESS_STATUS = 200;
    public final static int ERROR_STATUS = 400;

    public RpcResult(Integer status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public RpcResult() {
    }

    public RpcResult<T> error(Integer status, String message, T data) {
        return new RpcResult<T>(status, message, data);
    }

    public RpcResult<T> error(String message) {
        return error(ERROR_STATUS, message, null);
    }

    public RpcResult<T> error() {
        return error("error");
    }


    public RpcResult<T> success(Integer status, String message, T data) {
        return new RpcResult<T>(status, message, data);
    }

    public RpcResult<T> success(String message, T data) {
        return success(SUCCESS_STATUS, message, data);
    }

    public RpcResult<T> success(T data) {
        return success("success", data);
    }

    public RpcResult<T> success() {
        return success("success", null);
    }
}
