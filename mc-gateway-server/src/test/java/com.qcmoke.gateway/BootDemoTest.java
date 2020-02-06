package com.qcmoke.gateway;

import com.qcmoke.gateway.properties.GatewayAuthProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BootDemoTest {

    @Autowired
    private GatewayAuthProperties gatewayAuthProperties;

    @Test
    public void test1() {
        System.out.println(gatewayAuthProperties.getIgnoreAuthenticateUrl());
    }
}
