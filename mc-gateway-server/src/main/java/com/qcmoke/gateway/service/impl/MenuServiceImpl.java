package com.qcmoke.gateway.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.qcmoke.common.service.RedisService;
import com.qcmoke.gateway.dao.MenuDao;
import com.qcmoke.gateway.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author qcmoke
 */
@Service
public class MenuServiceImpl implements MenuService {
    private final static String GATEWAY_ALL_ACL_METADATA_KEY = "gateway.all.acl.metadata.key";
    /**
     * 缓存一天
     */
    private final static Long GATEWAY_ALL_ACL_METADATA_TIME = 24 * 3600L;

    @Autowired
    private RedisService redisService;
    @Autowired
    private MenuDao menuDao;

    /**
     * 返回的格式为
     * {
     * aclUrl1 : ["role1","role2"],
     * aclUrl2 : ["role1","role3"]
     * }
     */
    @Override
    public Map<String, Set<String>> getMetadataMap() {
        Map<String, Set<String>> metadataMap = null;
        Object cache = redisService.get(GATEWAY_ALL_ACL_METADATA_KEY);
        //在JSON.parseObject第一个参数传递字符串，第二个参数new TypeReference<>(){},<>里面写你的目标类型
        Map<String, Set<String>> cacheAllApiRoles = JSON.parseObject(JSON.toJSONString(cache, SerializerFeature.WriteMapNullValue), new TypeReference<Map<String, Set<String>>>() {
        });
        if (cacheAllApiRoles != null && !cacheAllApiRoles.isEmpty()) {
            metadataMap = cacheAllApiRoles;
        } else {
            List<Map<String, Object>> databaseAllApiRoles = menuDao.selectAllApiRolesArrays();
            metadataMap = formatApiRoles(databaseAllApiRoles);
            redisService.set(GATEWAY_ALL_ACL_METADATA_KEY, metadataMap, GATEWAY_ALL_ACL_METADATA_TIME);
        }
        return metadataMap;
    }

    public LinkedHashMap<String, Set<String>> formatApiRoles(List<Map<String, Object>> mapList) {
        LinkedHashMap<String, Set<String>> linkedHashMap = new LinkedHashMap<>();
        mapList.forEach(apiAndRoleMap -> {
            String api = (String) apiAndRoleMap.get("api");
            String roleName = (String) apiAndRoleMap.get("roleName");
            if (linkedHashMap.containsKey(api)) {
                Set<String> needRolesExist = linkedHashMap.get(api);
                needRolesExist.add(roleName);
                linkedHashMap.put(api, needRolesExist);
            } else {
                Set<String> needRoles = new HashSet<>();
                needRoles.add(roleName);
                linkedHashMap.put(api, needRoles);
            }
        });
        return linkedHashMap;
    }
}
