package com.qcmoke.ums.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;


/**
 * @author qcmoke
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailVo implements Serializable {

    /**
     * 用户ID
     */
    private Long uid;

    /**
     * 用户名
     */
    private String username;


    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 状态 0锁定 1有效
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 最近访问时间
     */
    private Date lastLoginTime;

    /**
     * 性别 0男 1女 2保密
     */
    private String sex;

    /**
     * 是否开启tab，0关闭 1开启
     */
    private String isTab;

    /**
     * 主题
     */
    private String theme;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 描述
     */
    private String description;


    private String roleNames;

    private Set<String> authorities;

    private Set<String> permissions;
}
