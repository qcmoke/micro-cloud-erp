package com.qcmoke.pms.client;

import com.qcmoke.common.constant.GlobalServiceListConstant;
import com.qcmoke.common.vo.Result;
import com.qcmoke.ums.api.UserApi;
import com.qcmoke.ums.vo.UserVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author qcmoke
 */
@FeignClient(value = GlobalServiceListConstant.MC_UMS_SERVER_ID, fallbackFactory = UserClient.UserClientFallbackFactory.class)
public interface UserClient extends UserApi {
    @Slf4j
    @Component
    class UserClientFallbackFactory implements FallbackFactory<UserClient> {
        @Override
        public UserClient create(Throwable throwable) {
            log.error("触发降级处理,e={}", throwable.getMessage());
            return new UserClient() {
                @Override
                public Result<UserVo> getUserById(Long userId) {
                    return Result.error(throwable.getMessage());
                }

                @Override
                public Result<List<UserVo>> getUserByIds(String[] userIds) {
                    return Result.error(throwable.getMessage());
                }

            };
        }
    }
}
