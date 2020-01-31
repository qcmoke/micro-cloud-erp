package com.qcmoke.system.service.impl;

import com.qcmoke.system.dao.UserDetailsDao;
import com.qcmoke.system.entity.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDetailsDao userDetailsDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = userDetailsDao.findByName(username);
        if (authUser == null) {
            throw new UsernameNotFoundException("账号不存在, username= " + username);
        }
        return authUser;
    }

}
