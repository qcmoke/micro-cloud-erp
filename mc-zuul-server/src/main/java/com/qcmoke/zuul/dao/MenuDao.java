package com.qcmoke.zuul.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class MenuDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JSONArray selectAllApiRolesArrays() {
        String sql = "" +
                "   SELECT DISTINCT m.api as api,r.role_name as roleName" +
                "	FROM t_menu m" +
                "	LEFT JOIN t_role_menu rm ON ( rm.menu_id = m.menu_id )" +
                "	LEFT JOIN t_role r ON ( r.role_id = rm.role_id ) " +
                "	WHERE" +
                "	m.api IS NOT NULL" +
                "	AND m.api <> ''";

        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        return JSONArray.parseArray(JSONObject.toJSONString(list, SerializerFeature.WriteMapNullValue));
    }
}