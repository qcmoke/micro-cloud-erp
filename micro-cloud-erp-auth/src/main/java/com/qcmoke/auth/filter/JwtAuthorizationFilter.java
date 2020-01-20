package com.qcmoke.auth.filter;


import com.qcmoke.auth.utils.JwtUtil;
import com.qcmoke.core.utils.RespBean;
import com.qcmoke.core.utils.ResponseWriterUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * 在WebSecurityConfig中禁用session后，就不会存在返回sessionid给客户端cookie，因此就不能确保会话的状态，
 * 那么springsecurity就不能识别每次请求的用户是否是已经登录认证过的了，那么就需要客户端每次请求都要携带token，通过token给springsecurity设置用户名和角色等信息，以此来保证每次请求的状态，而不是保证整个会话的状态。
 * <p>
 * <p>
 * 所有的其他请求都会来到这个方法中
 * 在这个方法中，提取出客户端传来的 JWT Token，并进行解析，解析后得到的用户、角色等信息封装到UsernamePasswordAuthenticationToken中，并设置到SecurityContext上下文中。不需要设置密码，因为验证过的jwt token的数据已经确定用户的安全性
 */

@Component
public class JwtAuthorizationFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //从Header里获取token
        String jwtToken = ((HttpServletRequest) servletRequest).getHeader(JwtUtil.REQUEST_TOKEN_NAME);
        //如果解析 token 成功并且本次会话的权限还未被写入
        if (StringUtils.isNotBlank(jwtToken) && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) JwtUtil.getAuthentication(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);//SecurityContextHolder利用了一个SecurityContextHolderStrategy(具体实现org.springframework.security.core.context.ThreadLocalSecurityContextHolderStrategy)（存储策略）进行上下文的存储，而ThreadLocalSecurityContextHolderStrategy通过ThreadLocal为每个线程开辟一个存储区域（即SecurityContext安全上下文），来存储相应的对象。
            }
            //以下的异常处理，根据具体业务来做处理
            catch (ExpiredJwtException e) {
                ResponseWriterUtil.writeJson(RespBean.error("token过期,ExpiredJwtException"));
                return;
            } catch (UnsupportedJwtException e) {
                ResponseWriterUtil.writeJson(RespBean.error("token不支持,UnsupportedJwtException"));
                return;
            } catch (MalformedJwtException e) {
                ResponseWriterUtil.writeJson(RespBean.error("token格式错误,MalformedJwtException"));
                return;
            } catch (SignatureException e) {
                ResponseWriterUtil.writeJson(RespBean.error("token签名异常,SignatureException"));
                return;
            } catch (IllegalArgumentException e) {
                ResponseWriterUtil.writeJson(RespBean.error("token非法,IllegalArgumentException"));
                return;
            }
        }
        //1.如果token为null，那么不会执行SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);所以springsecurity会认为当前请求的用户未登录认证，所以会重定向到登录页面(login_p)；需要提供AuthenticationEntryPoint来处理为登录认证的异常,否则会跳转到login_p
        //2.如果token不为null，不管是不是本人的token，只要验证token正确都可通过，如果不是本人的token也不用担心，放行后让springsecurity自己来授权拦截即可。
        filterChain.doFilter(servletRequest, servletResponse);

        //处理完请求和相应后清空当前用户的权限上下文
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}

