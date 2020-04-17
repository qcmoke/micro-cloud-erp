package com.qcmoke.ums.dto;

import com.qcmoke.common.annotation.IsMobile;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author qcmoke
 */
@Data
public class UserDto {
    /**
     * 用户名
     */
    @Size(min = 4, max = 10, message = "{range}")
    private String username;

    /**
     * 密码
     */
    private String password;
    private Long deptId;
    @NotBlank(message = "{required}")
    private String roleId;
    @NotBlank(message = "{required}")
    private String sex;
    private String roleName;
    @Size(max = 100, message = "{noMoreThan}")
    private String description;
    private String avatar;
    private Integer userId;
    // @NotBlank(message = "{required}")
    private Integer status;
    @Size(max = 50, message = "{noMoreThan}")
    @Email(message = "{email}")
    private String email;
    @IsMobile(message = "{mobile}")
    private String mobile;
}
