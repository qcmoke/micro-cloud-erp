package com.qcmoke.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.common.dto.PageQuery;
import com.qcmoke.common.vo.CurrentUser;
import com.qcmoke.ums.dto.UserDto;
import com.qcmoke.ums.entity.User;
import com.qcmoke.ums.vo.PageResult;
import com.qcmoke.ums.vo.UserDetailVo;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author qcmoke
 * @since 2020-02-12
 */
public interface UserService extends IService<User> {

    /**
     * 通过用户名获取用户详细信息
     *
     * @param username 用户名
     * @return 用户详细信息
     */
    UserDetailVo getUserDetailByUsername(String username);

    boolean updateAvatar(CurrentUser currentUser, String avatar);

    boolean updateUserInfo(User user);

    PageResult getPage(CurrentUser currentUser, PageQuery pageQuery);

    void updateUser(UserDto userDto);

    void createUser(UserDto userDto);

    void deleteUsers(String[] ids);

    void updatePassword(String password);

    void resetPassword(String[] usernameArr);
}
