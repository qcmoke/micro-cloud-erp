package com.qcmoke.auth;

import com.qcmoke.auth.properties.Oauth2SecurityProperties;
import com.qcmoke.common.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BootCacheTest {

    @Autowired
    private RedisService redisService;
    @Autowired
    private Oauth2SecurityProperties oauth2SecurityProperties;

    @Test
    public void test1() {

    }
}
