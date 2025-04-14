package com.nitb.uservocabularyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.nitb.uservocabularyservice", "com.nitb.common"})
public class UserVocabularyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserVocabularyServiceApplication.class, args);
    }

}
