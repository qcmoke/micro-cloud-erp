package com.qcmoke.ums.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.ums.dto.RoleDto;
import com.qcmoke.ums.entity.Role;
import com.qcmoke.ums.export.RoleExport;
import com.qcmoke.ums.vo.PageResult;

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

    void updateRole(RoleDto roleDto);

    PageResult getPage(PageQuery pageQuery, Role role);

    void createRole(RoleDto roleDto);

    void deleteRoles(String[] ids);

    IPage<RoleExport> findRoles(RoleDto roleDto, PageQuery pageQuery);
}
