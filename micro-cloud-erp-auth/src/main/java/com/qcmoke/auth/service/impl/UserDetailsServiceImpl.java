package com.qcmoke.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    UserDetails dbUser = new User(
            "qcmoke",
            "123456",
            AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN"));


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!username.equals(dbUser.getUsername())) {
            throw new UsernameNotFoundException("不存在该用户" + username);
        }
        return User.withUsername(dbUser.getUsername())
                .password(passwordEncoder.encode(dbUser.getPassword()))
                .authorities(dbUser.getAuthorities())
                .build();
    }

}
