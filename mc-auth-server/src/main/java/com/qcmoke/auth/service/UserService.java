package com.qcmoke.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.auth.dto.UserDetailVo;
import com.qcmoke.auth.entity.User;
import com.qcmoke.common.dto.CurrentUser;


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
}
