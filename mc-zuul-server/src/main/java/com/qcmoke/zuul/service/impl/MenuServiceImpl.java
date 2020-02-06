package com.qcmoke.zuul.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.qcmoke.common.service.RedisService;
import com.qcmoke.zuul.dao.MenuDao;
import com.qcmoke.zuul.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuServiceImpl implements MenuService {


    private final static String ALL_API_ROLES_ARRAY_KEY = "gateway.menu.all.api.roles.key";

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private RedisService redisService;

    @Override
    public JSONArray getAllApiRolesArrays() {
        if (redisService.hasKey(ALL_API_ROLES_ARRAY_KEY) && redisService.get(ALL_API_ROLES_ARRAY_KEY) != null) {
            return (JSONArray) redisService.get(ALL_API_ROLES_ARRAY_KEY);
        }
        JSONArray dbAllApiRoles = menuDao.selectAllApiRolesArrays();
        redisService.set(ALL_API_ROLES_ARRAY_KEY, dbAllApiRoles);
        return dbAllApiRoles;

    }
}
