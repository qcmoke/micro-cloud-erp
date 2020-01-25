package com.qcmoke.gateway.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface PermissionService {
    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
