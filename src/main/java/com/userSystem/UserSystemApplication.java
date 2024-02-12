package com.userSystem;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class UserSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserSystemApplication.class, args);
    }

    @Bean
    public OpenApiCustomiser openApiCustomiser() {
        return openApi -> openApi.getInfo().setVersion("1.0");
    }
}
