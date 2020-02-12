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
public class CurrentUser implements Serializable {
    private String username;
    private Set<String> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Long userId;
    private String avatar;
    private String email;
    private String mobile;
    private String sex;
    private Long deptId;
    private String deptName;
    private String roleId;
    private String roleName;
    private Date lastLoginTime;
    private String description;
    private String status;
}
