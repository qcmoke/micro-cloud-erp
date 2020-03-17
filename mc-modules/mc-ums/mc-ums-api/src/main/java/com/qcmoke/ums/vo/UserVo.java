package com.qcmoke.ums.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qcmoke
 */
@Data
public class UserVo implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;

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

}
