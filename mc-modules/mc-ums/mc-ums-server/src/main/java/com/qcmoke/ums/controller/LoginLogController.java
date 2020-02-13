package com.qcmoke.ums.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qcmoke.common.dto.Result;
import com.qcmoke.common.utils.oauth.OauthSecurityJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RequestMapping("/log/login")
@RestController
public class LoginLogController {
    @GetMapping("/me")
    public Result<Object> getUserLastSevenLoginLogs(HttpServletRequest request) {
        String currentUsername = OauthSecurityJwtUtil.getCurrentUsername(request);

        String json = "{" +
                "    \"data\": [" +
                "        {" +
                "            \"id\": 23," +
                "            \"username\": \"" + currentUsername + "\"," +
                "            \"loginTime\": \"2020-01-28 16:18:09\"," +
                "            \"location\": \"内网IP|0|0|内网IP|内网IP\"," +
                "            \"ip\": \"127.0.0.1\"," +
                "            \"system\": \"Windows 10\"," +
                "            \"browser\": \"Chrome 79\"," +
                "            \"loginTimeFrom\": null," +
                "            \"loginTimeTo\": null" +
                "        }," +
                "        {" +
                "            \"id\": 22," +
                "            \"username\": \"" + currentUsername + "\"," +
                "            \"loginTime\": \"2020-01-28 15:46:13\"," +
                "            \"location\": \"内网IP|0|0|内网IP|内网IP\"," +
                "            \"ip\": \"127.0.0.1\"," +
                "            \"system\": \"Windows 10\"," +
                "            \"browser\": \"Chrome 79\"," +
                "            \"loginTimeFrom\": null," +
                "            \"loginTimeTo\": null" +
                "        }," +
                "        {" +
                "            \"id\": 21," +
                "            \"username\": \"" + currentUsername + "\"," +
                "            \"loginTime\": \"2020-01-28 15:22:34\"," +
                "            \"location\": \"内网IP|0|0|内网IP|内网IP\"," +
                "            \"ip\": \"127.0.0.1\"," +
                "            \"system\": \"Windows 10\"," +
                "            \"browser\": \"Chrome 79\"," +
                "            \"loginTimeFrom\": null," +
                "            \"loginTimeTo\": null" +
                "        }," +
                "        {" +
                "            \"id\": 20," +
                "            \"username\": \"" + currentUsername + "\"," +
                "            \"loginTime\": \"2020-01-28 00:06:32\"," +
                "            \"location\": \"内网IP|0|0|内网IP|内网IP\"," +
                "            \"ip\": \"127.0.0.1\"," +
                "            \"system\": \"Windows 10\"," +
                "            \"browser\": \"Chrome 79\"," +
                "            \"loginTimeFrom\": null," +
                "            \"loginTimeTo\": null" +
                "        }," +
                "        {" +
                "            \"id\": 19," +
                "            \"username\": \"" + currentUsername + "\"," +
                "            \"loginTime\": \"2020-01-27 21:39:28\"," +
                "            \"location\": \"内网IP|0|0|内网IP|内网IP\"," +
                "            \"ip\": \"127.0.0.1\"," +
                "            \"system\": \"\"," +
                "            \"browser\": \"\"," +
                "            \"loginTimeFrom\": null," +
                "            \"loginTimeTo\": null" +
                "        }," +
                "        {" +
                "            \"id\": 18," +
                "            \"username\": \"" + currentUsername + "\"," +
                "            \"loginTime\": \"2020-01-27 21:38:41\"," +
                "            \"location\": \"内网IP|0|0|内网IP|内网IP\"," +
                "            \"ip\": \"127.0.0.1\"," +
                "            \"system\": \"\"," +
                "            \"browser\": \"\"," +
                "            \"loginTimeFrom\": null," +
                "            \"loginTimeTo\": null" +
                "        }," +
                "        {" +
                "            \"id\": 17," +
                "            \"username\": \"" + currentUsername + "\"," +
                "            \"loginTime\": \"2020-01-27 21:38:26\"," +
                "            \"location\": \"内网IP|0|0|内网IP|内网IP\"," +
                "            \"ip\": \"127.0.0.1\"," +
                "            \"system\": \"\"," +
                "            \"browser\": \"\"," +
                "            \"loginTimeFrom\": null," +
                "            \"loginTimeTo\": null" +
                "        }" +
                "    ]" +
                "}";

        JSONObject jsonObject = JSON.parseObject(json);
        Object data = jsonObject.get("data");
        return Result.ok(data);
    }
}
