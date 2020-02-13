package com.qcmoke.ums;

import com.qcmoke.ums.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BootUserTest {


    @Autowired
    private UserService userService;

    @Test
    public void test1() {
        System.out.println(userService.getUserDetailByUsername("qcmoke"));
    }
}
