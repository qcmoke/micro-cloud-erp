package com.qcmoke.ums.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qcmoke.ums.entity.Role;
import com.qcmoke.ums.export.RoleExport;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author qcmoke
 * @since 2020-02-12
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    @Select("   <script>" +
            "   SELECT" +
            "        r.role_id roleId," +
            "        r.role_name roleName," +
            "        r.role_name_zh roleNameZh," +
            "        r.remark," +
            "        r.create_time createTime," +
            "        r.modify_time modifyTime," +
            "        GROUP_CONCAT( rm.menu_id ) menuIds" +
            "        FROM" +
            "        t_role r" +
            "        LEFT JOIN t_role_menu rm ON ( r.role_id = rm.role_id )" +
            "        WHERE 1 = 1" +
            "        <if test=\"role.roleName != null and role.roleName != ''\">" +
            "            AND r.role_name like CONCAT('%',#{role.roleName},'%')" +
            "        </if>" +
            "        GROUP BY" +
            "        r.role_id" +
            "   </script>")
    IPage<Map<String, Object>> getPage(Page<?> page, @Param("role") Role role);

    @Select("   <script>" +
            "   SELECT" +
            "        r.role_id roleId," +
            "        r.role_name roleName," +
            "        r.role_name_zh roleNameZh," +
            "        r.remark," +
            "        r.create_time createTime," +
            "        r.modify_time modifyTime," +
            "        GROUP_CONCAT( rm.menu_id ) menuIds" +
            "        FROM" +
            "        t_role r" +
            "        LEFT JOIN t_role_menu rm ON ( r.role_id = rm.role_id )" +
            "        WHERE 1 = 1" +
            "        <if test=\"role.roleName != null and role.roleName != ''\">" +
            "            AND r.role_name like CONCAT('%',#{role.roleName},'%')" +
            "        </if>" +
            "        GROUP BY" +
            "        r.role_id" +
            "   </script>")
    IPage<RoleExport> findRolePage(Page<Role> page, @Param("role") Role role);
}
