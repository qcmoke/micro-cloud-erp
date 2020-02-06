package com.qcmoke.ums.cotroller;

import com.qcmoke.common.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {

    /**
     * 获取认证完成的用户信息
     */
    @GetMapping("/detail")
    public Principal currentUser(Principal principal) {
        log.info("获取用户信息Principal={}", principal);
        return principal;
    }


    @GetMapping("/index")
    public Result<Object> index() throws ParseException {
        Map<String, Object> data = new HashMap<>();
        // 获取系统访问记录
        data.put("totalVisitCount", 11);
        data.put("todayVisitCount", 11);
        data.put("todayIp", 1111111);

        // 获取近期系统访问记录
        data.put("lastTenVisitCount", list);
        data.put("lastTenUserVisitCount", list);
        return Result.ok(data);
    }


    private ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>() {{
        add(new HashMap<String, Object>() {{
            put("10-01", 12);
        }});
        add(new HashMap<String, Object>() {{
            put("10-02", 22);
        }});
        add(new HashMap<String, Object>() {{
            put("10-03", 31);
        }});
        add(new HashMap<String, Object>() {{
            put("10-04", 4);
        }});
        add(new HashMap<String, Object>() {{
            put("10-05", 31);
        }});
        add(new HashMap<String, Object>() {{
            put("10-06", 31);
        }});
        add(new HashMap<String, Object>() {{
            put("10-07", 7);
        }});
        add(new HashMap<String, Object>() {{
            put("10-08", 8);
        }});
        add(new HashMap<String, Object>() {{
            put("10-09", 9);
        }});
        add(new HashMap<String, Object>() {{
            put("10-10", 10);
        }});
    }};
}
