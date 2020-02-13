package com.qcmoke.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.auth.dto.UserDetailVo;
import com.qcmoke.auth.entity.User;
import com.qcmoke.auth.mapper.MenuMapper;
import com.qcmoke.auth.mapper.UserMapper;
import com.qcmoke.auth.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
