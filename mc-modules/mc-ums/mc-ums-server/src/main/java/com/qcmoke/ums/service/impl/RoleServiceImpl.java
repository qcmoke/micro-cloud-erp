package com.qcmoke.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.utils.BeanCopyUtil;
import com.qcmoke.ums.dto.RoleDto;
import com.qcmoke.ums.entity.Role;
import com.qcmoke.ums.entity.RoleMenu;
import com.qcmoke.ums.entity.UserRole;
import com.qcmoke.ums.export.RoleExport;
import com.qcmoke.ums.mapper.RoleMapper;
import com.qcmoke.ums.service.RoleMenuService;
import com.qcmoke.ums.service.RoleService;
import com.qcmoke.ums.service.UserRoleService;
import com.qcmoke.ums.utils.SqlUtil;
import com.qcmoke.ums.vo.PageResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuService roleMenuService;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public IPage<RoleExport> findRoles(RoleDto roleDto, PageQuery pageQuery) {
        Page<Role> page = new Page<>(pageQuery.getPageNum(), pageQuery.getPageSize());
        SqlUtil.handlePageSort(pageQuery, "createTime", PageQuery.ORDER_DESC, false);
        return this.baseMapper.findRolePage(page, BeanCopyUtil.copy(roleDto, Role.class));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteRoles(String[] roleIds) {
        List<String> idList = Arrays.asList(roleIds);
        baseMapper.deleteBatchIds(idList);
        this.roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getRoleId, idList));
        this.userRoleService.remove((new LambdaQueryWrapper<UserRole>().in(UserRole::getRoleId, idList)));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createRole(RoleDto roleDto) {
        Role role = BeanCopyUtil.copy(roleDto, Role.class);
        role.setRoleId(null);
        role.setCreateTime(new Date());
        this.save(role);
        if (StringUtils.isNotBlank(roleDto.getMenuIds())) {
            String[] menuIds = StringUtils.splitByWholeSeparatorPreserveAllTokens(roleDto.getMenuIds(), ",");
            saveRoleMenus(role, menuIds);
        }
    }

    private void saveRoleMenus(Role role, String[] menuIds) {
        List<RoleMenu> roleMenus = new ArrayList<>();
        Arrays.stream(menuIds).forEach(menuId -> {
            RoleMenu roleMenu = new RoleMenu();
            if (StringUtils.isNotBlank(menuId)) {
                roleMenu.setMenuId(Long.valueOf(menuId));
            }
            roleMenu.setRoleId(role.getRoleId());
            roleMenus.add(roleMenu);
        });
        this.roleMenuService.saveBatch(roleMenus);
    }

    @Override
    public List<Role> findAllRoles() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Role::getRoleId);
        return this.baseMapper.selectList(queryWrapper);
    }


    @Override
    public PageResult getPage(PageQuery pageQuery, Role role) {
        Page<?> page = SqlUtil.handlePageSort(pageQuery, "createTime", PageQuery.ORDER_ASC, false);
        IPage<Map<String, Object>> iPage = roleMapper.getPage(page, role);
        PageResult pageResult = new PageResult();
        pageResult.setRows(iPage.getRecords());
        pageResult.setTotal(iPage.getTotal());
        return pageResult;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRole(RoleDto roleDto) {
        Role role = BeanCopyUtil.copy(roleDto, Role.class);
        role.setRoleName(null);
        role.setModifyTime(new Date());
        baseMapper.updateById(role);
        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, role.getRoleId()));
        if (StringUtils.isNotBlank(roleDto.getMenuIds())) {
            String[] menuIds = StringUtils.splitByWholeSeparatorPreserveAllTokens(roleDto.getMenuIds(), ",");
            saveRoleMenus(role, menuIds);
        }
    }
}
