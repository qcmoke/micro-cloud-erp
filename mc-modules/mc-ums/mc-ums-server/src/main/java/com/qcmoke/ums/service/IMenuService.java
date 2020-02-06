package com.qcmoke.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.ums.vo.VueRouter;
import com.qcmoke.ums.entity.Menu;

import java.util.List;

public interface IMenuService extends IService<Menu> {

    String findUserPermissions(String username);

    List<Menu> findUserMenus(String username);

    List<VueRouter<Menu>> getUserRouters(String username);

}
