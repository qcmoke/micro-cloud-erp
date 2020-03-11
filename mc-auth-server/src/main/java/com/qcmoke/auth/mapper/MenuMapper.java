package com.qcmoke.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qcmoke.auth.entity.Menu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Set;

/**
 * @author qcmoke
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 获取用户角色集合
     *
     * @param username 用户名
     * @return 用户权限
     */
    @Select("   SELECT DISTINCT r.role_name" +
            "   FROM t_role r" +
            "            LEFT JOIN t_user_role ur ON (r.role_id = ur.role_id)" +
            "            LEFT JOIN t_user u ON (u.user_id = ur.user_id)" +
            "            LEFT JOIN t_role_menu rm ON (rm.role_id = r.role_id)" +
            "            LEFT JOIN t_menu m ON (m.menu_id = rm.menu_id)" +
            "   WHERE u.username = #{username}" +
            "   AND m.perms IS NOT NULL" +
            "   AND m.perms <> ''")
    Set<String> getRoleNamesByUsername(@Param("username") String username);


    /**
     * 获取用户权限集合
     *
     * @param username 用户名
     * @return 用户权限
     */
    @Select("   select distinct m.perms" +
            "   from t_role r" +
            "            left join t_user_role ur on (r.role_id = ur.role_id)" +
            "            left join t_user u on (u.user_id = ur.user_id)" +
            "            left join t_role_menu rm on (rm.role_id = r.role_id)" +
            "            left join t_menu m on (m.menu_id = rm.menu_id)" +
            "   where u.username = #{username}" +
            "     and m.perms is not null" +
            "     and m.perms <> ''")
    Set<String> findUserPermissions(@Param("username") String username);
}