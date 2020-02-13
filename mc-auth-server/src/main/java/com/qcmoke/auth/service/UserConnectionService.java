package com.qcmoke.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qcmoke.auth.entity.UserConnection;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author qcmoke
 * @since 2020-02-12
 */
public interface UserConnectionService extends IService<UserConnection> {

    List<UserConnection> selectByCondition(String username);

    UserConnection selectByCondition(String toString, String uuid);
}
