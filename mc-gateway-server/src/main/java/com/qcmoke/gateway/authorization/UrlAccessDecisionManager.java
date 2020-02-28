package com.qcmoke.gateway.authorization;


import com.qcmoke.common.vo.CurrentUser;
import org.springframework.stereotype.Component;

import java.util.Collection;


/**
 * @author qcmoke
 */
@Component
public class UrlAccessDecisionManager {

    public boolean decide(CurrentUser currentUser, Collection<String> urlNeedRoles) {
        for (String needRole : urlNeedRoles) {
            if ("ROLE_LOGIN".equals(needRole)) {
                return true;
            }
            //当前用户所具有的角色列表
            Collection<String> authorities = currentUser.getAuthorities();
            for (String authority : authorities) {
                //判断请求url需要的角色与用户所具有的角色相等的话即可通过
                if (authority.equals(needRole)) {
                    return true;
                }
            }
        }
        return false;
    }
}