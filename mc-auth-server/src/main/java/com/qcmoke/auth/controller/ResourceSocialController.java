package com.qcmoke.auth.controller;

import com.alibaba.fastjson.JSONObject;
import com.qcmoke.auth.constant.SocialConstant;
import com.qcmoke.auth.entity.UserConnection;
import com.qcmoke.auth.exception.SocialException;
import com.qcmoke.auth.properties.Oauth2SocialProperties;
import com.qcmoke.auth.service.SocialLoginService;
import com.qcmoke.auth.vo.BindUserVo;
import com.qcmoke.common.dto.Result;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.List;

/**
 * @author qcmoke
 */
@Slf4j
@RequestMapping("/resource/social")
@Controller
public class ResourceSocialController {

    @Autowired
    private Oauth2SocialProperties oauth2SocialProperties;

    @Autowired
    private SocialLoginService socialLoginService;

    /**
     * 根据用户名获取绑定关系
     *
     * @param username 用户名
     * @return Result
     */
    @ResponseBody
    @GetMapping("/connections/{username}")
    public Result<Object> findUserConnections(@NotBlank(message = "{required}") @PathVariable String username) {
        List<UserConnection> userConnections = this.socialLoginService.findUserConnections(username);
        return Result.ok(userConnections);
    }


    /**
     * 向第三方发送认证授权请求
     * http://127.0.0.1:9070/auth/resource/social/render/github/login
     */
    @GetMapping("/render/{socialType}/{loginType}")
    public void renderAuth(@PathVariable("socialType") String socialType, @PathVariable("loginType") String loginType, HttpServletResponse response) throws IOException, SocialException {
        if (!SocialConstant.LOGIN_TYPE_LOGIN.equals(loginType) && !SocialConstant.LOGIN_TYPE_BIND.equals(loginType)) {
            log.error("socialType={},loginType={},e={}", socialType, loginType, "不支持该登录方式");
            throw new SocialException("不支持该登录方式");
        }

        //初始化授权
        AuthRequest authRequest = socialLoginService.getAuthRequest(socialType);
        //设置state并得到授权地址
        String authorizeUrl = authRequest.authorize(socialType + "::" + AuthStateUtils.createState() + "::" + loginType);
        //向第三方发送授权请求
        response.sendRedirect(authorizeUrl);
    }

    /**
     * 第三方登录回调
     */
    @GetMapping("/callback/{socialType}")
    public String callback(@PathVariable("socialType") String socialType, String state, AuthCallback callback, Model model) throws SocialException {
        JSONObject response = null;
        try {
            String loginType = StringUtils.substringAfterLast(state, "::");
            if (SocialConstant.LOGIN_TYPE_BIND.equals(loginType)) {
                response = socialLoginService.resolveBind(socialType, callback);
            } else {
                response = socialLoginService.resolveLogin(socialType, callback);
            }
            model.addAttribute("response", response);
            model.addAttribute("frontUrl", oauth2SocialProperties.getFrontUrl());
            log.info("model={}", model);
            return "result";
        } catch (Exception e) {
            log.error("e={}", e.getMessage());
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }


    /**
     * 绑定并登录
     *
     * @param bindUserVo bindUser
     * @param authUser authUser
     * @return FebsResponse
     */
    @ResponseBody
    @PostMapping("/bind/login")
    public Result<Object> bindLogin(BindUserVo bindUserVo, AuthUser authUser) throws SocialException {
        OAuth2AccessToken oAuth2AccessToken = socialLoginService.bindLogin(bindUserVo, authUser);
        return Result.ok(oAuth2AccessToken);
    }


    /**
     * 注册并登录
     *
     * @param registerUser registerUser
     * @param authUser     authUser
     * @return FebsResponse
     */
    @ResponseBody
    @PostMapping("/sign/login")
    public Result<Object> signLogin(@Valid BindUserVo registerUser, AuthUser authUser) throws SocialException {
        OAuth2AccessToken oAuth2AccessToken = socialLoginService.signLogin(registerUser, authUser);
        return Result.ok(oAuth2AccessToken);
    }


    /**
     * 绑定
     *
     * @param bindUserVo bindUser
     * @param authUser authUser
     */
    @ResponseBody
    @PostMapping("/bind")
    public void bind(BindUserVo bindUserVo, AuthUser authUser) throws SocialException {
        this.socialLoginService.bind(bindUserVo, authUser);
    }

    /**
     * 解绑
     *
     * @param bindUserVo  bindUser
     * @param oauthType oauthType
     */
    @ResponseBody
    @DeleteMapping("/unbind")
    public void unbind(BindUserVo bindUserVo, String oauthType) throws SocialException {
        this.socialLoginService.unbind(bindUserVo, oauthType);
    }

}