package com.nitb.vocabularyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.nitb.vocabularyservice", "com.nitb.common"})
public class VocabularyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(VocabularyServiceApplication.class, args);
    }

}
