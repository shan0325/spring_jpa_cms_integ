package com.shan.spring.cms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
public class PasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Test
    public void 패스워드_암호화() {
        String secretPassword = passwordEncoder.encode("1234");
        System.out.println("secretPassword = " + secretPassword);
    }

}
