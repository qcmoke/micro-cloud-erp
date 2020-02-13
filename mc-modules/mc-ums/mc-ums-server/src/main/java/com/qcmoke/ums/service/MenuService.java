package com.qcmoke.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.ums.entity.Menu;
import com.qcmoke.ums.dto.VueRouter;

import java.util.List;

/**
 * @author qcmoke
 */
public interface MenuService extends IService<Menu> {

    String findUserPermissions(String username);

    List<Menu> findUserMenus(String username);

    List<VueRouter<Menu>> getUserRouters(String username);

}
