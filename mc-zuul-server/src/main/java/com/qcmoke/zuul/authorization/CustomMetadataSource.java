package com.qcmoke.zuul.authorization;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qcmoke.zuul.properties.ZuulAuthProperties;
import com.qcmoke.zuul.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.*;

/**
 * 给请求url赋予数据库中对应的角色列表，如果数据库中不存在对应的角色则给与该url一个ROLE_LOGIN角色
 * <p>
 * 两种情况:
 * 1.对于需要“登录+角色”的url，给该请求配置所有与之相关的角色
 * 2.对于仅仅只需要“登录”的url，可以统一规定需要ROLE_LOGIN角色
 */
@Component
public class CustomMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private MenuService menuService;
    AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Autowired
    private ZuulAuthProperties zuulAuthProperties;


    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) {
        String requestUrl = ((FilterInvocation) o).getRequestUrl();
        //免授权白名单
        String[] ignoreAuthorizationUrl = StringUtils.split(zuulAuthProperties.getIgnoreAuthorizationUrl(), ",");
        for (String ignoreUrl : ignoreAuthorizationUrl) {
            if (antPathMatcher.match(ignoreUrl, requestUrl)) {
                return null;
            }
        }

        JSONArray allMenuRoleMapList = menuService.getAllApiRolesArrays();
        Map<String, Collection<ConfigAttribute>> allApiRolesMap = getAllApiRolesMap(allMenuRoleMapList);
        for (Map.Entry<String, Collection<ConfigAttribute>> needRole : allApiRolesMap.entrySet()) {
            String aclUrl = needRole.getKey();
            if (antPathMatcher.match(aclUrl, requestUrl)) {
                return needRole.getValue();
            }
        }
        //没有匹配上的资源，都是登录访问，对于登录的角色可自定义为ROLE_LOGIN角色
        //如果返回null的话，意味着当前这个请求不需要任何角色就能访问，甚至不需要登录,即白名单。
        return SecurityConfig.createList("ROLE_LOGIN");
    }


    public Map<String, Collection<ConfigAttribute>> getAllApiRolesMap(JSONArray mapList) {
        Map<String, Collection<ConfigAttribute>> linkedHashMap = new LinkedHashMap<>();
        mapList.forEach(rolesAndMenu -> {
            JSONObject obj = (JSONObject) rolesAndMenu;
            String api = (String) obj.get("api");
            SecurityConfig roleName = new SecurityConfig((String) obj.get("roleName"));
            if (linkedHashMap.containsKey(api)) {
                Collection<ConfigAttribute> needRolesExist = linkedHashMap.get(api);
                needRolesExist.add(roleName);
                linkedHashMap.put(api, needRolesExist);
            } else {
                List<ConfigAttribute> needRoles = new ArrayList<>();
                needRoles.add(roleName);
                linkedHashMap.put(api, needRoles);
            }
        });
        return linkedHashMap;
    }


    /**
     * getAllConfigAttributes 方法用来返回所有定义好的权限资源， Spring Security 在启动时会校验
     * 相关配置是否正确 ，如果 需要校验，那么该方法直接返回 null 即可
     *
     * @return null
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    /**
     * 返回类对象是否支持校验。
     *
     * @param aClass
     * @return boolean
     */
    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
