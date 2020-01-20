package com.qcmoke.auth.service;

import com.qcmoke.auth.entity.Role;
import com.qcmoke.auth.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysUserDetailsService implements UserDetailsService {

    private static final ThreadLocal<List<User>> userListLocal = new ThreadLocal<>();


    private void initUserListLocal() {
        userListLocal.set(new ArrayList<>());
    }

    /**
     * 假设为数据库数据
     */
    private void initData() {
        Role userRole = new Role(1L, "ROLE_user", "ROLE_user");
        Role admin = new Role(2L, "ROLE_admin", "ROLE_admin");

        List<Role> adminRoles = new ArrayList<>();
        adminRoles.add(admin);
        List<User> userList = userListLocal.get();
        userList.add(new User("admin", "123", adminRoles));
        List<Role> sangRoles = new ArrayList<>();
        sangRoles.add(userRole);
        userList.add(new User("sang", "456", sangRoles));

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserbyUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }


    private User findUserbyUserName(String username) {
        initUserListLocal();
        initData();
        for (User user : userListLocal.get()) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
