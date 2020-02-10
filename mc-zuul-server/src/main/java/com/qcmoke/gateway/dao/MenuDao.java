package com.qcmoke.gateway.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MenuDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> selectAllApiRolesArrays() {
        String sql = "" +
                "   SELECT DISTINCT m.api as api,r.rname as roleName" +
                "	FROM t_menu m" +
                "	LEFT JOIN t_role_menu rm ON ( rm.mid = m.mid )" +
                "	LEFT JOIN t_role r ON ( r.rid = rm.rid ) " +
                "	WHERE" +
                "	m.api IS NOT NULL" +
                "	AND m.api <> ''";

        return jdbcTemplate.queryForList(sql);
    }
}