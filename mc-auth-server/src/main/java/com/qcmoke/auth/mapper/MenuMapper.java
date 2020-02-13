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
    @Select("   SELECT DISTINCT r.rname" +
            "   FROM t_role r" +
            "            LEFT JOIN t_user_role ur ON (r.rid = ur.rid)" +
            "            LEFT JOIN t_user u ON (u.uid = ur.uid)" +
            "            LEFT JOIN t_role_menu rm ON (rm.rid = r.rid)" +
            "            LEFT JOIN t_menu m ON (m.mid = rm.mid)" +
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
            "            left join t_user_role ur on (r.rid = ur.rid)" +
            "            left join t_user u on (u.uid = ur.uid)" +
            "            left join t_role_menu rm on (rm.rid = r.rid)" +
            "            left join t_menu m on (m.mid = rm.mid)" +
            "   where u.username = #{username}" +
            "     and m.perms is not null" +
            "     and m.perms <> ''")
    Set<String> findUserPermissions(@Param("username") String username);
}