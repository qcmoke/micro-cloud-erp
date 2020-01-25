package com.qcmoke.gateway.service.impl;

import com.qcmoke.gateway.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {
    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        log.info("uri={}", request.getRequestURI());
        log.info("authentication={}", ReflectionToStringBuilder.toString(authentication));

        //下面可以改成从数据库获取用户权限并进行权限校验
        boolean flag = RandomUtils.nextInt() % 2 == 0;

        if (!flag) {
            log.info("访问未授权");
        }
        return flag;
    }
}
