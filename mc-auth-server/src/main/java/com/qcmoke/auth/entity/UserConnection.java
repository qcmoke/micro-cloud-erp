package com.qcmoke.auth.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author qcmoke
 * @since 2020-02-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserConnection implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统用户名
     */
    private String username;

    /**
     * 第三方平台名称
     */
    private String providerName;

    /**
     * 第三方平台账户ID
     */
    private String providerUserId;

    /**
     * 第三方平台用户名
     */
    private String providerUsername;

    /**
     * 第三方平台昵称
     */
    private String nickName;

    /**
     * 第三方平台头像
     */
    private String imageUrl;

    /**
     * 地址
     */
    private String location;

    /**
     * 备注
     */
    private String remark;


}
