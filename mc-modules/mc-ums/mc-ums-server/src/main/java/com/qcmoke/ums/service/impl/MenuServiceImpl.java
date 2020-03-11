package com.qcmoke.ums.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qcmoke.common.utils.BeanCopyUtil;
import com.qcmoke.ums.constant.MenuConstant;
import com.qcmoke.ums.entity.Menu;
import com.qcmoke.ums.export.MenuExport;
import com.qcmoke.ums.mapper.MenuMapper;
import com.qcmoke.ums.service.MenuService;
import com.qcmoke.ums.utils.TreeUtil;
import com.qcmoke.ums.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author qcmoke
 */
@Slf4j
@Service("menuService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<MenuExport> findMenuList(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(menu.getMenuName())) {
            queryWrapper.like(Menu::getMenuName, menu.getMenuName());
        }
        queryWrapper.orderByAsc(Menu::getMenuId);
        List<Menu> menus = this.baseMapper.selectList(queryWrapper);
        return BeanCopyUtil.copy(menus, MenuExport.class);
    }

    @Override
    public PageResult getPage(Menu menu) {
        PageResult pageResult = new PageResult();
        try {
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.orderByAsc(Menu::getOrderNum);
            List<Menu> menus = baseMapper.selectList(queryWrapper);
            List<MenuTree> trees = new ArrayList<>();
            buildTrees(trees, menus);
            if (StringUtils.equals(menu.getType(), MenuConstant.TYPE_BUTTON)) {
                pageResult.setRows(trees);
            } else {
                List<? extends Tree<?>> menuTree = TreeUtil.build(trees);
                pageResult.setRows(menuTree);
            }
            pageResult.setTotal(menus.size());
        } catch (NumberFormatException e) {
            log.error("查询菜单失败", e);
            pageResult.setRows(null);
            pageResult.setTotal(0);
        }
        return pageResult;
    }

    private void buildTrees(List<MenuTree> trees, List<Menu> menus) {
        menus.forEach(menu -> {
            MenuTree tree = new MenuTree();
            tree.setId(menu.getMenuId().toString());
            tree.setParentId(menu.getParentId().toString());
            tree.setLabel(menu.getMenuName());
            tree.setComponent(menu.getComponent());
            tree.setIcon(menu.getIcon());
            tree.setOrderNum(menu.getOrderNum());
            tree.setPath(menu.getPath());
            tree.setType(menu.getType());
            tree.setPerms(menu.getPerms());
            trees.add(tree);
        });
    }

    @Override
    public String findUserPermissions(String username) {
        Set<String> permissions = this.baseMapper.findUserPermissions(username);
        return String.join(",", permissions);
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
            route.setId(menu.getMenuId().toString());
            route.setParentId(menu.getParentId().toString());
            route.setPath(menu.getPath());
            route.setComponent(menu.getComponent());
            route.setName(menu.getMenuName());
            route.setMeta(new RouterMeta(menu.getMenuName(), menu.getIcon(), true));
            routes.add(route);
        });
        //这时候的路由集合是没有层级结构的，可以通过TreeUtil的buildVueRouter方法，将路由集合转换为包含层级结构的路由信息。
        return TreeUtil.buildVueRouter(routes);
    }


}
