package com.qcmoke.order.service.impl;

import com.qcmoke.common.entity.Role;
import com.qcmoke.common.entity.User;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    User dbUser = new User(
            "qcmoke",
            "123456",
            Collections.singletonList(new Role(1L, "ROLE_ADMIN", "管理员")));


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!username.equals(dbUser.getUsername())) {
            throw new UsernameNotFoundException("不存在该用户" + username);
        }
        return dbUser;
    }

}
