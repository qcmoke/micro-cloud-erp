package com.qcmoke.ums.dto;

import lombok.Data;

/**
 * @author qcmoke
 */
@Data
public class UserDto {
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
    private Long deptId;
    private String roleId;
    private String sex;
    private String roleName;
    private String description;
    private String avatar;
    private Integer userId;
    private Integer status;
    private String email;
    private String mobile;
}
