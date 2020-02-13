package com.qcmoke.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.dto.CurrentUser;
import com.qcmoke.ums.dto.UserDetailVo;
import com.qcmoke.ums.entity.User;
import com.qcmoke.ums.mapper.MenuMapper;
import com.qcmoke.ums.mapper.UserMapper;
import com.qcmoke.ums.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
}
