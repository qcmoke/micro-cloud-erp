package com.qcmoke.ums.dto;

import com.qcmoke.ums.entity.Role;
import lombok.Data;

/**
 * @author qcmoke
 */
@Data
public class RoleDto extends Role {
    private transient String menuIds;
}
