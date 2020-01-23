/**
 *
 */
package com.qcmoke.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.qcmoke.core.entity.TokenInfo;
import com.qcmoke.core.utils.RespBean;
import com.qcmoke.core.utils.ResponseWriterUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 授权拦截过滤器
 */
@Slf4j
@Component
public class AuthorizationFilter extends ZuulFilter {

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 3;
    }


    @Override
    public Object run() throws ZuulException {
        log.info("authorization start");
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        //校验请求是否需要认证
        if (isNeedAuth(request)) {
            TokenInfo tokenInfo = (TokenInfo) request.getAttribute("tokenInfo");
            if (tokenInfo != null && tokenInfo.isActive()) {
                //校验token是否有权限请求(是否授权)
                if (!hasPermission(tokenInfo, request)) {
                    //无请求权限，授权失败
                    log.info("无请求权限，授权失败，audit log update fail 403");
                    ResponseWriterUtil.writeJson(RespBean.forbidden("无请求权限，授权失败，auth fail 403"));
                }
                //设置数据给网关后的服务
                requestContext.addZuulRequestHeader("username", tokenInfo.getUser_name());
                requestContext.setRequestQueryParams(new LinkedHashMap<String, List<String>>() {{
                    put("active", Collections.singletonList(Boolean.toString(tokenInfo.isActive())));
                    put("client_id", Collections.singletonList(tokenInfo.getClient_id()));
                    put("scope", Arrays.asList(tokenInfo.getScope()));
                    put("exp", Collections.singletonList(tokenInfo.getExp().toString()));
                    put("aud", Arrays.asList(tokenInfo.getAud()));
                    put("user_name", Collections.singletonList(tokenInfo.getUser_name()));
                }});
            } else {
                if (!StringUtils.startsWith(request.getRequestURI(), "/token")) {
                    //无token或者token无效，认证失败
                    log.info("无token或者token无效，认证失败，audit log update fail 401");
                    ResponseWriterUtil.writeJson(RespBean.unauthorized("无token或者token无效，认证失败的，auth fail 401"));
                }
            }
        }
        return null;
    }

    private boolean hasPermission(TokenInfo tokenInfo, HttpServletRequest request) {
        log.info("hasPermission: tokenInfo={}", tokenInfo);
//        return RandomUtils.nextInt() % 2 == 0;
        return true;
    }

    private boolean isNeedAuth(HttpServletRequest request) {
        return true;
    }

}
