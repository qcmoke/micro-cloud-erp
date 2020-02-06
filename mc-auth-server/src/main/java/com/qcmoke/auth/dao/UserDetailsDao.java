package com.qcmoke.auth.dao;

import com.qcmoke.auth.common.entity.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
public class UserDetailsDao {

    @Qualifier("systemJdbcTemplate")
    @Autowired
    private JdbcTemplate systemJdbcTemplate;


    public AuthUser findByName(String username) {
        String sql = "   SELECT" +
                "        u.user_id userId," +
                "        u.username," +
                "        u.email," +
                "        u.mobile," +
                "        u.password," +
                "        u.status," +
                "        u.create_time createTime," +
                "        u.ssex sex," +
                "        u.dept_id deptId," +
                "        u.last_login_time lastLoginTime," +
                "        u.modify_time modifyTime," +
                "        u.description," +
                "        u.avatar," +
                "        d.dept_name deptName," +
                "        GROUP_CONCAT(r.role_id) roleId," +
                "        GROUP_CONCAT(r.ROLE_NAME) roleName" +
                "        FROM t_user u" +
                "        LEFT JOIN t_dept d ON (u.dept_id = d.dept_id)" +
                "        LEFT JOIN t_user_role ur ON (u.user_id = ur.user_id)" +
                "        LEFT JOIN t_role r ON r.role_id = ur.role_id" +
                "        WHERE  u.username = ?" +
                "        group by u.username,u.user_id,u.email,u.mobile,u.password, u.status,u.create_time,u.ssex,u.dept_id,u.last_login_time,u.modify_time,u.description,u.avatar";
        Map<String, Object> userMap = systemJdbcTemplate.queryForMap(sql, username);

        String permissions = findUserPermissions(username);
        AuthUser authUser = new AuthUser(
                (String) userMap.get("username"),
                (String) userMap.get("password"),
                true,
                true,
                true,
                "1".equals(userMap.get("status")),
                AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
        try {
            BeanUtils.populate(authUser, userMap);
        } catch (Exception e) {
            log.info("BeanUtils.copyProperties(userMap, authUser) e={}", e.getMessage());
            throw new RuntimeException("系统发生异常：e=" + e.getMessage());
        }
        return authUser;
    }


    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;
        Object obj = beanClass.newInstance();
        org.apache.commons.beanutils.BeanUtils.populate(obj, map);
        return obj;
    }


    private String findUserPermissions(String username) {
        String sql = "  SELECT " +
                "       DISTINCT m.perms,r.role_name as roleName" +
                "       FROM t_role r" +
                "                LEFT JOIN t_user_role ur ON (r.role_id = ur.role_id)" +
                "                LEFT JOIN t_user u ON (u.user_id = ur.user_id)" +
                "                LEFT JOIN t_role_menu rm ON (rm.role_id = r.role_id)" +
                "                LEFT JOIN t_menu m ON (m.menu_id = rm.menu_id)" +
                "       WHERE u.username = ?" +
                "       AND m.perms IS NOT NULL" +
                "       AND m.perms <> ''";
        List<Map<String, Object>> list = systemJdbcTemplate.queryForList(sql, username);

        ArrayList<String> userPermissions = new ArrayList<>();
        list.forEach(map -> {
            //userPermissions.add((String) map.get("perms"));
            userPermissions.add((String) map.get("roleName"));
        });
        return String.join(",", userPermissions);
    }
}
