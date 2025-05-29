package com.nitb.authservice.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailConfig {
    private final Dotenv dotenv = Dotenv.load();

    @Bean
    public String getMailUsername() {
        return dotenv.get("MAIL_USERNAME");
    }

    @Bean
    public String getMailPassword() {
        return dotenv.get("MAIL_PASSWORD");
    }
}
