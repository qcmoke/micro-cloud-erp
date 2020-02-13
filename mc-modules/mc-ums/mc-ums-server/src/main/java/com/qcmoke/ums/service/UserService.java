package com.qcmoke.ums.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.common.dto.CurrentUser;
import com.qcmoke.ums.dto.UserDetailVo;
import com.qcmoke.ums.entity.User;

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
}
