package com.qcmoke.ums.controller;

import com.alibaba.fastjson.JSONObject;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.CurrentUser;
import com.qcmoke.common.vo.Result;
import com.qcmoke.ums.entity.User;
import com.qcmoke.ums.service.UserService;
import com.qcmoke.ums.vo.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.util.Date;

/**
 * @author qcmoke
 */
@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping
    public Result<PageResult> getPage(PageQuery pageQuery) {
        CurrentUser currentUser = OauthSecurityJwtUtil.getCurrentUser();
        PageResult dataTable = userService.getPage(currentUser, pageQuery);
        return Result.ok(dataTable);
    }

    @GetMapping("/success")
    public Result<Object> success(HttpServletRequest request) {
        String currentUsername = OauthSecurityJwtUtil.getCurrentUsername(request);
        log.info("记录登录成功日志,currentUsername={}", currentUsername);
        return Result.ok();
    }


    /**
     * 修改个人信息
     *
     * @param user
     */
    @PutMapping("/profile")
    public Result<Object> updateUserInfo(@Valid User user, HttpServletRequest request) {
        Long currentUserId = OauthSecurityJwtUtil.getCurrentUserId(request);
        user.setUid(currentUserId);
        user.setModifyTime(new Date());
        boolean isUpdated = this.userService.updateUserInfo(user);
        if (!isUpdated) {
            return Result.error();
        }
        return Result.ok();
    }


    /**
     * 修改用戶头像
     *
     * @param avatar
     * @param request
     * @return
     */
    @PutMapping("/avatar")
    public Result<Object> updateAvatar(@NotBlank(message = "{required}") String avatar, HttpServletRequest request) {
        User user = new User();
        user.setAvatar(avatar);
        CurrentUser currentUser = OauthSecurityJwtUtil.getCurrentUser(request);
        boolean isUpdated = userService.updateAvatar(currentUser, avatar);
        if (!isUpdated) {
            return Result.error();
        }
        return Result.ok();
    }


    @GetMapping("/index")
    public Result<Object> index() throws ParseException {
        String json = "{" +
                "  \"data\": {" +
                "    \"lastTenUserVisitCount\": [" +
                "      { \"count\": 1, \"days\": \"02-09\" }," +
                "      { \"count\": 1, \"days\": \"02-10\" }," +
                "      { \"count\": 1, \"days\": \"02-11\" }," +
                "      { \"count\": 1, \"days\": \"02-12\" }," +
                "      { \"count\": 1, \"days\": \"02-13\" }" +
                "    ]," +
                "    \"totalVisitCount\": 26," +
                "    \"todayVisitCount\": 3," +
                "    \"todayIp\": 1," +
                "    \"lastTenVisitCount\": [" +
                "      { \"count\": 2, \"days\": \"02-09\" }," +
                "      { \"count\": 2, \"days\": \"02-10\" }," +
                "      { \"count\": 2, \"days\": \"02-11\" }," +
                "      { \"count\": 2, \"days\": \"02-12\" }," +
                "      { \"count\": 3, \"days\": \"02-13\" }" +
                "    ]" +
                "  }" +
                "}";

        JSONObject jsonObject = JSONObject.parseObject(json);
        Object data = jsonObject.get("data");
        return Result.ok(data);
    }
}
