package com.jica.sdg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SdgApplication {

    public static void main(String[] args) {
        SpringApplication.run(SdgApplication.class, args);
//        BCryptPasswordEncoder b = new BCryptPasswordEncoder();
//        System.out.println(b.encode("super"));
//        System.out.println(b.encode("admin"));
//        System.out.println(b.encode("user"));
    }

}
