package com.qcmoke.gateway.handler;

import com.qcmoke.core.utils.RespBean;
import com.qcmoke.core.utils.ResponseWriterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 授权异常处理-->处理匿名token(不传token)用户或者非法token用户访问无权限资源时的异常
 */
@Slf4j
@Component
public class GatewayAuthenticationEntryPointHandler extends OAuth2AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        //super.commence(request, response, e);
        if (e instanceof AccessTokenRequiredException) {
            excuteSql(String.format("update log set status = '%s', log_auth_fail_msg = '%s',update_date='%s' where username = '%s'", "fail", "无token", "anonymousUser", new Date().toLocaleString()));
        } else {
            excuteSql(String.format("insert into log(status,username,log_auth_fail_msg,create_date) values('%s','%s','%s','%s')", "fail", "null", "token无效", new Date().toLocaleString()));
        }

        String msg = "无token或者token无效，认证失败的，auth fail 401！e=" + e.getMessage();
        log.error(msg);
        ResponseWriterUtil.writeJson(RespBean.unauthorized(msg));
    }

    private void excuteSql(String sql) {
        System.out.println(sql);
    }
}
