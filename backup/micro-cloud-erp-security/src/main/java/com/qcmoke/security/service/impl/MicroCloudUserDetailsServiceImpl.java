package com.qcmoke.security.service.impl;


import com.qcmoke.security.entity.Role;
import com.qcmoke.security.entity.User;
import com.qcmoke.security.service.QcmokeUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wenxiaohua
 * @version v1.0
 * @description demo
 * @date 2019年11月11日 02:01
 */
@Service
public class MicroCloudUserDetailsServiceImpl implements QcmokeUserDetailsService {
    List<Role> roles = new ArrayList<>();
    List<User> users = new ArrayList<>();

    public void connDb() {
        roles.add((new Role().setId(1L).setName("ROLE_user").setNameZh("普通用户")));
        users.add(new User()
                .setUsername("qcmoke")
                .setPassword("$2a$10$Z0K53ZIUGEVS3N91KeUPu.BlbCrXKmckMkW1F7LDpef3.OQZbsUPy")
                .setMobile("13012345670")
                .setRoles(roles)
        );

        roles.add(new Role().setId(2L).setName("ROLE_admin").setNameZh("管理员"));
        users.add(new User()
                .setUsername("admin")
                .setPassword("$2a$10$Z0K53ZIUGEVS3N91KeUPu.BlbCrXKmckMkW1F7LDpef3.OQZbsUPy")
                .setMobile("13012345678")
                .setRoles(roles)
        );
    }

    /**
     * 表单登录时从数据库获取UserDetails用户信息
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        connDb();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        throw new UsernameNotFoundException("Not Found User");
    }

    /**
     * 短信认证登录时获取用户信息
     *
     * @param mobile
     * @return
     */
    @Override
    public UserDetails loadUserByMobile(String mobile) {
        connDb();
        for (User user : users) {
            if (user.getMobile().equals(mobile)) {
                return user;
            }
        }
        throw new UsernameNotFoundException("Not Found User");
    }
}