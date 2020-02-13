package com.qcmoke.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcmoke.ums.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    Set<String> findUserPermissions(String userName);

    List<Menu> findUserMenus(String userName);
}