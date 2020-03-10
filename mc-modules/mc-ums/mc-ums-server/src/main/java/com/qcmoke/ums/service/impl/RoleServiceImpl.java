package com.qcmoke.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.ums.dto.RoleDto;
import com.qcmoke.ums.entity.Role;
import com.qcmoke.ums.entity.RoleMenu;
import com.qcmoke.ums.mapper.RoleMapper;
import com.qcmoke.ums.service.RoleMenuService;
import com.qcmoke.ums.service.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-02-12
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public List<Role> findAllRoles() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Role::getRid);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public void updateRole(RoleDto roleDto) {

    }
}
