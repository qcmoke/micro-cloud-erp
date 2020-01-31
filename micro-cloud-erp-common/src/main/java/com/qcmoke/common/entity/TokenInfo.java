package com.qcmoke.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TokenInfo implements Serializable {

    /**
     * 令牌是否可用
     */
    private boolean active;

    /**
     * 发送令牌的客户端id
     */
    private String client_id;

    /**
     * 令牌的scope集合
     */
    private String[] scope;

    /**
     * 用户名
     */
    private String user_name;

    /**
     * 可访问的资源服务器集合
     */
    private String[] aud;

    /**
     * 令牌的过期时间
     */
    private Date exp;

    /**
     * 令牌对应用户的所有权限
     */
    private String[] authorities;

}
