package com.qcmoke.common.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnResult<T> implements Serializable {
    //状态码
    private int status = 200;
    //提示信息
    private String message;
    //返回数据
    private T data;


    public ReturnResult<T> data(T data) {
        this.data = data;
        return this;
    }

    public ReturnResult<T> status(int status) {
        this.status = status;
        return this;
    }

    public ReturnResult<T> message(String message) {
        this.message = message;
        return this;
    }
}
