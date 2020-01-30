package com.qcmoke.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcmoke.system.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> findUserPermissions(String userName);

    List<Menu> findUserMenus(String userName);
}