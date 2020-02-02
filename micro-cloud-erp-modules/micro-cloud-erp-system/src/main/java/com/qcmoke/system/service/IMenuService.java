package com.qcmoke.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.common.entity.router.VueRouter;
import com.qcmoke.system.entity.Menu;

import java.util.List;
import java.util.Map;

public interface IMenuService extends IService<Menu> {

    String findUserPermissions(String username);

    List<Menu> findUserMenus(String username);

    List<VueRouter<Menu>> getUserRouters(String username);

}
