package com.qcmoke.ums.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.ums.entity.Menu;
import com.qcmoke.ums.mapper.MenuMapper;
import com.qcmoke.ums.service.MenuService;
import com.qcmoke.ums.utils.TreeUtil;
import com.qcmoke.ums.vo.RouterMeta;
import com.qcmoke.ums.vo.VueRouter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author qcmoke
 */
@Slf4j
@Service("menuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public String findUserPermissions(String username) {
        List<Menu> userPermissions = this.baseMapper.findUserPermissions(username);
        return userPermissions.stream().map(Menu::getPerms).collect(Collectors.joining(","));
    }

    @Override
    public List<Menu> findUserMenus(String username) {
        return this.baseMapper.findUserMenus(username);
    }


    @Override
    public List<VueRouter<Menu>> getUserRouters(String username) {
        List<VueRouter<Menu>> routes = new ArrayList<>();
        List<Menu> menus = this.findUserMenus(username);
        if (CollectionUtils.isEmpty(menus)) {
            return Collections.emptyList();
        }
        //将菜单对象一一转换为路由对象
        menus.forEach(menu -> {
            VueRouter<Menu> route = new VueRouter<>();
            route.setId(menu.getMid().toString());
            route.setParentId(menu.getParentId().toString());
            route.setPath(menu.getPath());
            route.setComponent(menu.getComponent());
            route.setName(menu.getMname());
            route.setMeta(new RouterMeta(menu.getMname(), menu.getIcon(), true));
            routes.add(route);
        });
        //这时候的路由集合是没有层级结构的，可以通过TreeUtil的buildVueRouter方法，将路由集合转换为包含层级结构的路由信息。
        return TreeUtil.buildVueRouter(routes);
    }


}
