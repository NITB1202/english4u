package com.nitb.usertestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.nitb.usertestservice", "com.nitb.common"})
public class UserTestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserTestServiceApplication.class, args);
    }

}
