package com.qcmoke.pms.utils;

import com.qcmoke.common.vo.Result;
import com.qcmoke.pms.client.UserClient;
import com.qcmoke.ums.vo.UserVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class UserClientUtil {
    /**
     * 通过id从UserClient获取用户列表
     */
    public static List<UserVo> getUserVoList(Set<Long> userIdSet, UserClient userClient) {
        List<UserVo> userVoList = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(userIdSet)) {
            String[] userIds = userIdSet.stream().map(x -> x + "").toArray(String[]::new);
            Result<List<UserVo>> listResult = userClient.getUserByIds(userIds);
            if (listResult.getStatus() == HttpStatus.OK.value()) {
                userVoList = listResult.getData();
            }
        }
        return userVoList;
    }
}
