package com.nitb.authservice;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication(scanBasePackages = {"com.nitb.authservice", "com.nitb.common"})
public class AuthServiceApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(AuthServiceApplication.class)
                .initializers((ApplicationContextInitializer<ConfigurableApplicationContext>) context -> {
                    Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
                    dotenv.entries().forEach(entry ->
                            System.setProperty(entry.getKey(), entry.getValue())
                    );
                })
                .run(args);
    }
}
