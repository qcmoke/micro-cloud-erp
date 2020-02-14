package com.qcmoke.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.auth.dto.UserDetailDto;
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
    public UserDetailDto getUserDetailByUsername(String username) {
        UserDetailDto userDetailDto = userMapper.selectUserDetailByUsername(username);
        if (userDetailDto == null) {
            return null;
        }
        String roleNames = userDetailDto.getRoleNames();
        String[] authorities = StringUtils.split(roleNames, ",");
        userDetailDto.setAuthorities(new HashSet<>(Arrays.asList(authorities)));
        Set<String> permissions = menuMapper.findUserPermissions(username);
        userDetailDto.setPermissions(permissions);
        return userDetailDto;
    }
}
