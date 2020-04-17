package com.qcmoke.ums.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.utils.BeanCopyUtil;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import com.qcmoke.common.vo.CurrentUser;
import com.qcmoke.common.vo.Result;
import com.qcmoke.ums.api.UserApi;
import com.qcmoke.ums.dto.UserDto;
import com.qcmoke.ums.entity.User;
import com.qcmoke.ums.service.UserService;
import com.qcmoke.ums.vo.PageResult;
import com.qcmoke.ums.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author qcmoke
 */
@Slf4j
@RequestMapping("/user")
@RestController
public class UserController implements UserApi {

    @Autowired
    private UserService userService;

    @Override
    public Result<List<UserVo>> getUserByIds(String[] userIds) {
        List<String> userIdList = Arrays.asList(userIds);
        List<User> userList = this.userService.list(new LambdaQueryWrapper<User>().in(User::getUserId, userIdList));
        List<UserVo> userVoList = BeanCopyUtil.copy(userList, UserVo.class);
        return Result.ok(userVoList);
    }

    @Override
    public Result<UserVo> getUserById(Long userId) {
        User user = userService.getById(userId);
        UserVo userVo = BeanCopyUtil.copy(user, UserVo.class);
        return Result.ok(userVo);
    }

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
     * 创建用户
     *
     * @param userDto
     */
    @PostMapping
    public void addUser(@Valid UserDto userDto) {
        this.userService.createUser(userDto);
    }

    /**
     * 修改用户信息
     *
     * @param userDto
     */
    @PutMapping
    public void updateUser(@Valid UserDto userDto) {
        this.userService.updateUser(userDto);
    }


    /**
     * 批量删除
     *
     * @param userIds
     */
    @DeleteMapping("/{userIds}")
    public void deleteUsers(@NotBlank(message = "{required}") @PathVariable String userIds) {
        String[] ids = userIds.split(StringPool.COMMA);
        this.userService.deleteUsers(ids);
    }


    /**
     * 修改个人信息
     *
     * @param user
     */
    @PutMapping("/profile")
    public Result<Object> updateUserInfo(@Valid User user, HttpServletRequest request) {
        Long currentUserId = OauthSecurityJwtUtil.getCurrentUserId(request);
        user.setUserId(currentUserId);
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

    /**
     * 修改密码
     *
     * @param password
     */
    @PutMapping("/password")
    public void updatePassword(@NotBlank(message = "{required}") String password) {
        userService.updatePassword(password);
    }

    /**
     * 重置密码
     *
     * @param usernames
     */
    @PutMapping("/password/reset")
    public void resetPassword(@NotBlank(message = "{required}") String usernames) {
        String[] usernameArr = usernames.split(StringPool.COMMA);
        this.userService.resetPassword(usernameArr);
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
