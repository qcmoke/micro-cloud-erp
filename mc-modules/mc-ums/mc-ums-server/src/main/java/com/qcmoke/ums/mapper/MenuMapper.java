package com.qcmoke.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcmoke.ums.entity.Menu;

import java.util.List;


public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> findUserPermissions(String userName);

    List<Menu> findUserMenus(String userName);
}