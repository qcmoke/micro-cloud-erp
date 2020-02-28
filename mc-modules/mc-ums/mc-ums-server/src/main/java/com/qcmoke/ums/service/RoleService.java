package com.qcmoke.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.ums.entity.Role;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author qcmoke
 * @since 2020-02-12
 */
public interface RoleService extends IService<Role> {

    List<Role> findAllRoles();

}
