package com.qcmoke.auth.service;

import com.qcmoke.auth.entity.Menu;
import com.qcmoke.auth.entity.Role;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    private static final ThreadLocal<List<Menu>> menusLocal = new ThreadLocal<>();


    private void initMenusLocal() {
        menusLocal.set(new ArrayList<>());
    }


    /**
     * 假设为数据库数据
     */
    private void initData() {
        Role user = new Role(1L, "ROLE_user", "ROLE_user");
        Role admin = new Role(2L, "ROLE_admin", "ROLE_admin");


        Menu menu1 = new Menu();
        menu1.setUrl("/hello");
        List<Role> roles = new ArrayList<>();
        roles.add(user);
        menu1.setRoles(roles);

        Menu menu2 = new Menu();
        menu2.setUrl("/admin");
        List<Role> roles2 = new ArrayList<>();
        roles2.add(admin);
        menu2.setRoles(roles2);


        Menu menu3 = new Menu();
        menu3.setUrl("/test");
        List<Role> roles3 = new ArrayList<>();
        roles3.add(admin);
        menu3.setRoles(roles3);


        List<Menu> menus = menusLocal.get();
        menus.add(menu1);
        menus.add(menu2);
        menus.add(menu3);
    }


    public List<Menu> getAllMenu() {
        initMenusLocal();
        initData();
        return menusLocal.get();
    }
}
