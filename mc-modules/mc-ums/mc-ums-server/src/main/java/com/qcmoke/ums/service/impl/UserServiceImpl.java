package com.qcmoke.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.utils.BeanCopyUtil;
import com.qcmoke.common.vo.CurrentUser;
import com.qcmoke.ums.dto.UserDto;
import com.qcmoke.ums.entity.User;
import com.qcmoke.ums.entity.UserRole;
import com.qcmoke.ums.mapper.MenuMapper;
import com.qcmoke.ums.mapper.UserMapper;
import com.qcmoke.ums.service.UserRoleService;
import com.qcmoke.ums.service.UserService;
import com.qcmoke.ums.utils.SqlUtil;
import com.qcmoke.ums.vo.PageResult;
import com.qcmoke.ums.vo.UserDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author qcmoke
 * @since 2020-02-12
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private UserRoleService userRoleService;

    @Override
    public UserDetailVo getUserDetailByUsername(String username) {
        UserDetailVo userDetailVo = userMapper.selectUserDetailByUsername(username);
        if (userDetailVo == null) {
            return null;
        }
        String roleNames = userDetailVo.getRoleNames();
        String[] authorities = StringUtils.split(roleNames, ",");
        userDetailVo.setAuthorities(new HashSet<>(Arrays.asList(authorities)));
        Set<String> permissions = menuMapper.findUserPermissions(username);
        userDetailVo.setPermissions(permissions);
        return userDetailVo;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
    @Override
    public boolean updateAvatar(CurrentUser currentUser, String avatar) {
        User user = this.baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, currentUser.getUsername()));
        user.setAvatar(avatar);
        int update = this.baseMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, currentUser.getUsername()));
        return update > 0;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
    @Override
    public boolean updateUserInfo(User updateUser) {
        int update = this.baseMapper.updateById(updateUser);
        return update > 0;
    }


    @Override
    public PageResult getPage(CurrentUser currentUser, PageQuery pageQuery) {
        User user = BeanCopyUtil.copy(currentUser, User.class);
        Page<?> page = SqlUtil.handlePageSort(pageQuery, "userId", PageQuery.ORDER_ASC, false);
        IPage<Map<String, Object>> iPage = this.baseMapper.getPage(page, user);
        PageResult pageResult = new PageResult();
        pageResult.setRows(iPage.getRecords());
        pageResult.setTotal(iPage.getTotal());
        return pageResult;
    }

    // 默认头像
    public static final String DEFAULT_AVATAR = "default.jpg";

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createUser(UserDto userDto) {
        // 创建用户
        User user = BeanCopyUtil.copy(userDto, User.class);
        user.setAvatar(DEFAULT_AVATAR);
        this.save(user);
        // 保存用户角色
        String[] roleNames = userDto.getRoleId().split(StringPool.COMMA);
        List<UserRole> userRoles = new ArrayList<>();
        Arrays.stream(roleNames).forEach(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(Long.valueOf(roleId));
            userRoles.add(userRole);
        });
        userRoleService.saveBatch(userRoles);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUser(UserDto userDto) {
        // 更新用户
        User user = new User();
        user.setUserId(userDto.getUserId().longValue());
        user.setModifyTime(new Date());
        user.setSex(userDto.getSex());
        user.setEmail(userDto.getEmail());
        user.setStatus(userDto.getStatus());
        user.setMobile(userDto.getMobile());
        user.setDeptId(userDto.getDeptId());
        updateById(user);

        //替换用户角色
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getUserId()));
        String[] roleNames = userDto.getRoleId().split(StringPool.COMMA);
        List<UserRole> userRoles = new ArrayList<>();
        Arrays.stream(roleNames).forEach(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(Long.valueOf(roleId));
            userRoles.add(userRole);
        });
        userRoleService.saveBatch(userRoles);
    }
}
