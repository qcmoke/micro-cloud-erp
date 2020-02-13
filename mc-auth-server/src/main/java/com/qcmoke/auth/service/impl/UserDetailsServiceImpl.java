package com.qcmoke.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qcmoke.auth.common.entity.AuthUserDetails;
import com.qcmoke.auth.constant.ParamsConstant;
import com.qcmoke.auth.constant.SocialConstant;
import com.qcmoke.auth.entity.User;
import com.qcmoke.auth.mapper.MenuMapper;
import com.qcmoke.auth.mapper.UserMapper;
import com.qcmoke.auth.properties.Oauth2SocialProperties;
import com.qcmoke.common.utils.SpringContextUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;


/**
 * @author qcmoke
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private Oauth2SocialProperties oauth2SocialProperties;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("账号不存在, username= " + username);
        }
        boolean accountNonLocked = Integer.valueOf(1).equals(user.getStatus());
        Set<String> permissionsSet = menuMapper.getRoleNamesByUsername(username);
        String permissions = String.join(",", permissionsSet);

        String password = user.getPassword();

        //第三方登录另作处理
        HttpServletRequest httpServletRequest = SpringContextUtil.getHttpServletRequest();
        String loginType = (String) httpServletRequest.getAttribute(ParamsConstant.LOGIN_TYPE);

        if (StringUtils.equals(loginType, SocialConstant.SOCIAL_LOGIN)) {
            password = passwordEncoder.encode(oauth2SocialProperties.getSocialUserPassword());
        }

        return new AuthUserDetails(user.getUsername(), password, user.getUid(), user.getStatus(), true, true, true, accountNonLocked,
                AuthorityUtils.createAuthorityList(permissions));
    }

}
