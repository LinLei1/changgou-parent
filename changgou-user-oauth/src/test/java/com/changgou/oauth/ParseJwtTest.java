package com.changgou.oauth;

import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class ParseJwtTest {

    @Test
    public void test(){
        String encode = new BCryptPasswordEncoder().encode("changgou");
        String code = new BCryptPasswordEncoder().encode("changgou");
        System.out.println(encode);
        System.out.println(code);
    }
}
