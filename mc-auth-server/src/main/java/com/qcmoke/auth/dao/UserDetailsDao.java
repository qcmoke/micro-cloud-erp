package com.qcmoke.auth.dao;

import com.qcmoke.auth.common.entity.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author qcmoke
 */
@Slf4j
@Repository
public class UserDetailsDao {

    @Qualifier("systemJdbcTemplate")
    @Autowired
    private JdbcTemplate systemJdbcTemplate;


    public AuthUser findByName(String username) {
        String sql = "SELECT uid, username, password, status from t_user WHERE username = ?";
        Map<String, Object> userMap = systemJdbcTemplate.queryForMap(sql, username);
        String permissions = findUserPermissions(username);
        String usernameDb = (String) userMap.get("username");
        String password = (String) userMap.get("password");
        Long uid = (Long) userMap.get("uid");
        Integer status = (Integer) userMap.get("status");
        boolean accountNonLocked = Integer.valueOf(1).equals(status);
        return new AuthUser(usernameDb, password, uid, status, true, true, true, accountNonLocked,
                AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
    }

    private String findUserPermissions(String username) {
        String sql = "  SELECT " +
                "       DISTINCT m.perms,r.rname" +
                "       FROM t_role r" +
                "                LEFT JOIN t_user_role ur ON (r.rid = ur.rid)" +
                "                LEFT JOIN t_user u ON (u.uid = ur.uid)" +
                "                LEFT JOIN t_role_menu rm ON (rm.rid = r.rid)" +
                "                LEFT JOIN t_menu m ON (m.mid = rm.mid)" +
                "       WHERE u.username = ?" +
                "       AND m.perms IS NOT NULL" +
                "       AND m.perms <> ''";
        List<Map<String, Object>> list = systemJdbcTemplate.queryForList(sql, username);

        ArrayList<String> userPermissions = new ArrayList<>();
        list.forEach(map -> {
            //userPermissions.add((String) map.get("perms"));
            userPermissions.add((String) map.get("rname"));
        });
        return String.join(",", userPermissions);
    }
}
