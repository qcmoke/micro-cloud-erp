package com.qcmoke.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SecretTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testSecret() {
        System.out.println(passwordEncoder.encode("123456"));
    }
}
