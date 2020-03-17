package com.qcmoke.ums.api;

import com.qcmoke.common.vo.Result;
import com.qcmoke.ums.vo.UserVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * @author qcmoke
 */
@RequestMapping("/user")
public interface UserApi {

    @GetMapping("/{userId}")
    Result<UserVo> getUserById(@PathVariable("userId") Long userId);

    @GetMapping("/getUserByIds")
    Result<List<UserVo>> getUserByIds(@RequestParam("userIds") String[] userIds);
}

