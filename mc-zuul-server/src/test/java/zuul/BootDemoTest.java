package com.qcmoke.zuul;

import com.qcmoke.zuul.properties.ZuulAuthProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BootDemoTest {

    @Autowired
    private ZuulAuthProperties zuulAuthProperties;

    @Test
    public void test1() {
        System.out.println(zuulAuthProperties.getIgnoreAuthenticateUrl());
    }
}
